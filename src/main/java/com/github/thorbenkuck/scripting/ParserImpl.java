package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.packages.Package;

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
	private final Register internalVariables = Register.create();
	// TODO: vlt mit strategyPattern dynamisch austauschbar machen
	private final AtomicInteger linePointer = new AtomicInteger();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Queue<Consumer<Register>> created = new LinkedList<>();
	private final AtomicBoolean linePointerFreeze = new AtomicBoolean(false);
	private static int currentFunctionCount = 0;
	private String errorMessage;

	private int getNextFunctionCount() {
		synchronized (this) {
			return currentFunctionCount++;
		}
	}

	private boolean applyRules(final Line line, final Queue<Consumer<Register>> created) {
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

	private String sliceFunctionNameAndParametrise(Line line) {
		StringBuilder functionName = new StringBuilder();

		line.trimLeadingWhiteSpaces();

		while (! (line.startsWith(" ") || line.startsWith("("))) {
			functionName.append(line.toString().toCharArray()[0]);
			line.remove(0);
		}

		// for something like:
		// "print ()" this
		// trim is needed again
		line.trimLeadingWhiteSpaces();
		line.remove(0);
		line.removeLast();

		return functionName.toString();
	}

	private void applyFunctions(final Line line) {
		Line functionComplete = sliceMostOuterFunction(line);
		Line reference = functionComplete.duplicate();
		String functionName = sliceFunctionNameAndParametrise(functionComplete);
		Line value = functionComplete.duplicate();
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
			-- beginOfFunction;
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

		created.add(Utility.wrapFunction(function, registerAddress, args));

		return registerAddress;
	}

	private String[] getParameter(Line args) {
		// This check is used, if recursive called
		// The following line
		// example(test(x));
		// would result in an call of
		// getParameter("(x)");
		if (args.startsWith("(")) {
			args.remove(0);
			if (args.endsWith(")")) {
				args.remove(args.length() - 1);
			}
		}

		final List<String> results = new ArrayList<>();

		if (isFunction(args)) {
			results.add(evaluateFunction(sliceFunctionNameAndParametrise(args), getParameter(args), args.getLineNumber()));
		} else {
			final String[] parameters = getArgumentsOfFunction(args);

			for (String parameter : parameters) {
				Line newLine = Line.create(parameter, args.getLineNumber());
				if (isFunction(newLine)) {
					results.add(evaluateFunction(sliceFunctionNameAndParametrise(newLine), getParameter(newLine), args.getLineNumber()));
				} else {
					results.add(parameter);
				}
			}
		}
		return results.toArray(new String[results.size()]);
	}

	private String spliceNextFunction(Line line) {
		int currentBracketStand = 0;
		StringBuilder currentArgument = new StringBuilder();
		for (char character : line) {
			if (character == '(') {
				++ currentBracketStand;
				currentArgument.append(character);
			} else if (character == ')') {
				-- currentBracketStand;
				currentArgument.append(character);
				if(currentBracketStand == 0) {
					line.remove(0, currentArgument.length() - 1);
					return currentArgument.toString();
				}
			} else {
				currentArgument.append(character);
			}
		}
		return "NULL";
	}

	private String[] spliceAllFunction(Line line) {
		List<String> results = new ArrayList<>();

		while(containsFunction(line.toString())) {
			results.add(spliceNextFunction(line));
		}

		return results.toArray(new String[results.size()]);
	}

	private String[] getArgumentsOfFunction(Line line) {
		List<String> results = new ArrayList<>();

		StringBuilder currentArgument = new StringBuilder();
		while(!line.isEmpty()) {
			if (startsWithFunction(line.toString())) {
				results.add(spliceNextFunction(line));
			} else if (line.startsWith(",") || line.startsWith(" ")) {
				line.remove(0);
			} else {
				for (char character : line) {
					if (character == ',') {
						break;
					} else {
						currentArgument.append(character);
					}
				}
				results.add(currentArgument.toString());
				line.remove(0, currentArgument.length() - 1);
				currentArgument = new StringBuilder();
			}
		}

		return results.toArray(new String[results.size()]);
	}

	private boolean isFunction(Line line) {
		if (! line.matches("[a-zA-Z0-9_][a-zA-Z0-9_-]+\\(.*")) {
			return false;
		}

		boolean foundLastClosingBracket = false;
		int bracketCount = 0;
		for (char currentChar : line) {
			if (currentChar == ')') {
				-- bracketCount;
				if (bracketCount == 0) {
					foundLastClosingBracket = true;
				}
			} else if (currentChar == '(') {
				// We found an function,
				// nested within the provided line
				// now check, if we found a
				// separate function or "only"
				// another function as an parameter
				// to our function.
				if (foundLastClosingBracket) {
					return false;
				} else {
					++ bracketCount;
				}
			} else if (foundLastClosingBracket && currentChar != ' ') {
				// We already found the end of our function
				// If we now find a char, that
				// is not an ' ', we should return
				// false, because we found another
				// char next to our function
				// and the provided line therefor
				// is NO stand alone function
				return false;
			}
		}
		return true;
	}

	private boolean startsWithFunction(String toCheck) {
		return toCheck.matches("[a-zA-Z0-9_][a-zA-Z0-9_-]+[ ]*\\(.*\\).*");
	}

	private boolean containsFunction(String toCheck) {
		return toCheck.matches(".*[a-zA-Z0-9_][a-zA-Z0-9_-]+[ ]*\\(.*\\).*");
	}

	private void preEvaluation(Collection<Line> lines) {
		for(Line line : lines) {
			int relation = relationOpenClosed(line);
			if(relation > 0) {
				error("Missing closing bracket(s): " + relation, line.getLineNumber());
			} else if(relation < 0) {
				error("To many closing bracket(s): " + -relation, line.getLineNumber());
			}
		}

		// Here we should check
		// for unknown functions,
		// as well as unknown rules.
		// The getUnknownFunctionNames
		// and getUnknownNames methods
		// are very tricky tho.. how
		// should this be achieved?
		// TODO
//		for(Line line : lines) {
//			List<String> unknownFunctionNames = getUnknownFunctionNames(lines);
//			if(unknownFunctionNames.size() > 0) {
//				error("Line contains undefined function(s): " + unknownFunctionNames, line.getLineNumber());
//				return;
//			}
//			List<String> unknownOtherNames = getUnknownNames(lines);
//			if(unknownOtherNames.size() > 0) {
//				error("I do not understand: " + unknownOtherNames, line.getLineNumber());
//				return;
//			}
//		}
	}

	private int relationOpenClosed(Line line) {
		// > 0 means more open than closed
		// < 0 means more closed than open
		return line.countCharInLine('(') - line.countCharInLine(')');
	}

	private void checkForError(List<Line> lines) throws ParsingFailedException {
		if (! running.get()) {
			throw new ParsingFailedException("Stopped while parsing Script!\n" + errorMessage + "\n> (" + linePointer.get() + "): " + lines.get(linePointer.get()));
		}
	}

	private int getCurrentLine() {
		if(linePointerFreeze.get()) {
			linePointerFreeze.set(false);
			return linePointer.get();
		}

		return linePointer.incrementAndGet();
	}

	@Override
	public synchronized Script parse(String string) throws ParsingFailedException {
		return parse(LineProvider.ofString(string));
	}

	@Override
	public synchronized Script parse(LineProvider lineProvider) throws ParsingFailedException {
		running.set(true);
		linePointerFreeze.set(false);
		List<Line> lines = lineProvider.provide();
		preEvaluation(lines);
		// We check here, to ensure basic
		// stuff works (i.e. every \\( is
		// closed within the same line)
		// This might be changed, to account
		// for multi-line brackets,
		// but it is way easier that way
		checkForError(lines);
		ScriptImpl result;
		linePointer.set(0);

		try {
			functionsLock.lock();
			ruleLock.lock();
			for (int currentLine = 0; currentLine < lines.size(); ) {
				Line line = lines.get(currentLine).duplicate();
				boolean workedOn = true;
				while (! line.isEmpty() && workedOn) {
					workedOn = false;
					try {
						if (containsFunction(line.toString())) {
							applyFunctions(line);
							workedOn = true;
						}
						applyRules(line, created);
					} catch (Exception e) {
						throw new ParsingFailedException("Encountered Exception while parsing!", e);
					}
				}

				// If an error occurs, we
				// want to detect it as early
				// as possible, so that we
				// do no unnecessary parsing
				// and take up resources, we
				// do not need in the end.
				// Therefor we check every time
				// if an error occurred. Just
				// then we increase the line counter
				checkForError(lines);
				currentLine = getCurrentLine();
			}

			// Here we assume, we parsed
			// correctly (since we threw
			// no exception). But to check,
			// for any potential error in
			// the last line, we have to
			// check again, if we
			// terminated ahead of time.
			checkForError(lines);
		} finally {
			functionsLock.unlock();
			ruleLock.unlock();
			synchronized (this) {
				internalVariables.clear();
				result = new ScriptImpl(created);
				created.clear();
				currentFunctionCount = 0;
				linePointer.set(0);
			}
			running.set(false);
		}

		return result;
	}

	@Override
	public void freezeLinePointer() {
		linePointerFreeze.set(true);
	}

	@Override
	public void setLinePointer(int to) {
		this.linePointer.set(to);
		this.linePointerFreeze.set(true);
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
	public void error(String message, int lineNumber) {
		running.set(false);
		linePointer.set(lineNumber);
		errorMessage = "error " + message;
	}

	@Override
	public void error(final String message) {
		error(message, linePointer.get());
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
	public void add(Function function) {
		try {
			functionsLock.lock();
			functions.put(function.getFunctionName(), function);
		} finally {
			functionsLock.unlock();
		}
	}

	@Override
	public void add(Rule rule) {
		try {
			ruleLock.lock();
			this.rules.add(rule);
		} finally {
			ruleLock.unlock();
		}
	}

	@Override
	public void add(Package newPackage) {
		for(Rule rule : newPackage.getRules()) {
			add(rule);
		}

		for(Function function : newPackage.getFunctions()) {
			add(function);
		}
	}

	@Override
	public String toString() {
		return "Parser{" +
				"rules=" + rules +
				", internalVariables=" + internalVariables +
				", linePointer=" + linePointer +
				", running=" + running +
				'}';
	}

}
