package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

class ParserImpl implements Parser {

	private final List<Rule> rules = new ArrayList<>();
	private final Map<String, Function> functions = new HashMap<>();
	private final Lock functionsLock = new ReentrantLock(true);
	private final Lock ruleLock = new ReentrantLock(true);
	private final Register internalVariables = new Register();
	// TODO: vlt mit strategyPattern dynamisch austauschbar machen
	private final String lineEnd = ";";
	private final AtomicInteger linePointer = new AtomicInteger();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Queue<Consumer<Register>> created = new LinkedList<>();
	private static int currentFunctionCount = 0;
	private String errorMessage;

	private int getNextFunctionCount() {
		synchronized (this) {
			return currentFunctionCount++;
		}
	}

	@Override
	public synchronized Script parse(String string) throws ParsingFailedException {
		running.set(true);
		List<Line> lines = parseLines(string);

		try {
			functionsLock.lock();
			ruleLock.unlock();
			for (int currentLine = 0; currentLine < lines.size(); ) {
				if (!running.get()) {
					throw new ParsingFailedException("Stopped while parsing Script!\n" + errorMessage + "\n> (" + linePointer.get() + "): " + lines.get(linePointer.get()));
				}
				Line line = lines.get(currentLine).duplicate();
				boolean workedOn = true;
				while (!line.isEmpty() && workedOn) {
					try {
						if (containsFunction(line.toString())) {
							applyFunction(line);
							workedOn = true;
						} else if (applyRule(line, created)) {
							workedOn = false;
						} else {
							workedOn = false;
						}
					} catch (Exception e) {
						throw new ParsingFailedException("Encountered Exception while parsing!", e);
					}
				}
				currentLine = linePointer.incrementAndGet();
			}
		} finally {
			functionsLock.unlock();
			ruleLock.unlock();
			ScriptImpl result;
			synchronized (this) {
				internalVariables.clear();
				result = new ScriptImpl(created);
				created.clear();
				currentFunctionCount = 0;
				linePointer.set(0);
			}
			running.set(false);

			return result;
		}
	}

	private boolean applyRule(final Line line, final Queue<Consumer<Register>> created) {
		boolean success = false;
		for (Rule rule : rules) {
			if (rule.applies(line)) {
				Consumer<Register> consumer = rule.apply(line, this, linePointer.get());
				if (consumer != null) {
					created.add(consumer);
					success = true;
				}
			}
		}

		return success;
	}

	private String sliceFunctionName(Line line) {
		StringBuilder functionName = new StringBuilder();

		line.trimLeadingWhiteSpaces();

		while (!(line.startsWith(" ") || line.startsWith("("))) {
			functionName.append(line.toString().toCharArray()[0]);
			line.remove(0);
		}

		// for something like:
		// "print ()" this
		// trim is needed again
		line.trimLeadingWhiteSpaces();

		return functionName.toString();
	}

	private void applyFunction(final Line line) {
		Line functionComplete = sliceMostOuterFunction(line);
		Line reference = functionComplete.duplicate();
		String functionName = sliceFunctionName(functionComplete);
		Line value = functionComplete.subpart(functionComplete.indexOf("(") + 1, functionComplete.lastIndexOf(")"));
		String[] args = getParameter(value);

		String address = evaluateFunction(functionName, args, line.getLineNumber());

		line.replace(reference.toString(), address);
	}

	private Line sliceMostOuterFunction(Line line) {
		int lastIndex = line.lastIndexOf(")") + 1;

		int beginOfFunction = line.indexOf("(");
		// remove the whiteSpaces
		// "print    (...)"
		// "     ^^^^
		while (line.getAt(beginOfFunction - 1) == ' ') {
			line.remove(beginOfFunction - 1);
		}

		while (beginOfFunction != 0 && line.getAt(beginOfFunction - 1) != ' ') {
			--beginOfFunction;
		}


		return line.subpart(beginOfFunction, lastIndex);
	}

	private String evaluateFunction(String functionName, String[] args, int lineNumber) {
		Function function = functions.get(functionName);
		if (function == null) {
			return "void";
		}
		function.onParse(args, this, lineNumber);
		String registerAddress = "F#" + functionName + getNextFunctionCount();

		created.add(new Consumer<Register>() {

			@Override
			public void accept(Register register) {
				register.put(registerAddress, function.calculate(args, register));
			}

			@Override
			public String toString() {
				return "Set register " + registerAddress + " to " + functionName + "(" + Arrays.toString(args) + ")";
			}
		});

		return registerAddress;
	}

	private String[] getParameter(Line args) {
		// This check is used, if recursive called
		// The following line
		// example(test(x));
		// would result in an call of
		// getParameter("(x)");
		if(args.startsWith("(")) {
			args.remove(0);
			if(args.endsWith(")")) {
				args.remove(args.length() - 1);
			}
		}
		final String[] potentialParameters = args.split(",[ ]");
		final List<String> results = new ArrayList<>();

		for (String parameter : potentialParameters) {
			Line newLine = new Line(parameter, args.getLineNumber());
			if (isFunction(newLine)) {
				results.add(evaluateFunction(sliceFunctionName(newLine), getParameter(newLine), args.getLineNumber()));
			} else {
				results.add(parameter);
			}
		}

		return results.toArray(new String[results.size()]);
	}

	private boolean isFunction(Line line) {
		return line.matches("[a-zA-Z0-9_][a-zA-Z0-9_-]+\\(.*\\).*");
	}

	private boolean containsFunction(String toCheck) {
		return toCheck.matches(".*[a-zA-Z0-9_][a-zA-Z0-9_-]+\\(.*\\).*");
	}

	private List<Line> parseLines(String wholeText) {
		List<Line> lines = new ArrayList<>();
		StringBuilder complete = new StringBuilder(wholeText);
		int lineNumber = 0;
		while (!complete.toString().isEmpty()) {
			String line;
			if(!complete.toString().contains(lineEnd)) {
				line = complete.toString();
			} else {
				line = complete.substring(0, complete.indexOf(lineEnd));
				complete.deleteCharAt(complete.indexOf(lineEnd));
			}
			while (line.startsWith(" ")) {
				line = line.substring(1, line.length());
				complete.deleteCharAt(0);
			}

			if (!line.isEmpty()) {
				lines.add(new Line(line, lineNumber++));
			}
			complete.delete(0, line.length());
		}

		return lines;
	}

	@Override
	public void setLinePointer(int to) {
		this.linePointer.set(to);
	}

	@Override
	public void setInternalVariable(String key, String value) {
		this.internalVariables.put(key, value);
	}

	@Override
	public String getInternalVariable(String key) {
		return this.internalVariables.get(key);
	}

	@Override
	public void addRule(Rule rule) {
		try {
			ruleLock.lock();
			this.rules.add(rule);
		} finally {
			ruleLock.unlock();
		}
	}

	@Override
	public void error(String s, int lineNumber) {
		running.set(false);
		linePointer.set(lineNumber);
		errorMessage = "error " + s;
	}

	@Override
	public void clearInternalVariable(String name) {
		internalVariables.remove(name);
	}

	@Override
	public void insert(Consumer<Register> consumer) {
		created.add(consumer);
	}

	@Override
	public void deleteInternalVariable(String name) {
		internalVariables.remove(name);
	}

	@Override
	public void addFunction(Function function) {
		try {
			functionsLock.lock();
			functions.put(function.getFunctionName(), function);
		} finally {
			functionsLock.unlock();
		}
	}

	@Override
	public String toString() {
		return "Parser{" +
				"rules=" + rules +
				", internalVariables=" + internalVariables +
				", lineEnd='" + lineEnd + '\'' +
				", linePointer=" + linePointer +
				", running=" + running +
				'}';
	}

}
