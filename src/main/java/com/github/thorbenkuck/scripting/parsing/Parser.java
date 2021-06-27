package com.github.thorbenkuck.scripting.parsing;

import com.github.thorbenkuck.keller.pipe.Pipeline;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.script.Script;
import com.github.thorbenkuck.scripting.script.ScriptElement;
import com.github.thorbenkuck.scripting.Utility;
import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.components.Rule;
import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.packages.Package;
import com.github.thorbenkuck.scripting.parsing.diagnostic.DiagnosticManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Parser {

	private final List<Rule> rules = new ArrayList<>();
	private final Map<String, Function> functions = new HashMap<>();
	private final Lock functionsLock = new ReentrantLock(true);
	private final Lock ruleLock = new ReentrantLock(true);
	private final Register internalVariables = new Register();
	private final AtomicBoolean running = new AtomicBoolean(false);
	private final Queue<ScriptElement> created = new LinkedList<>();
	private final Pipeline<ParsingFailedException> errorPipeline = Pipeline.unifiedCreation();
	private final AtomicReference<DiagnosticManager> diagnosticManagerReference;
	private final DefaultLineParser lineParser = new DefaultLineParser();
	private static int currentFunctionCount = 0;
	private final List<String> errorMessages = new ArrayList<>();
	private final List<Integer> faultyLines = new ArrayList<>();

	public Parser() {
		this(DiagnosticManager.systemErr());
	}

	public Parser(DiagnosticManager diagnosticManager) {
		this.diagnosticManagerReference = new AtomicReference<>(diagnosticManager);
	}

	private int getNextFunctionCount() {
		synchronized (this) {
			return currentFunctionCount++;
		}
	}

	private boolean applyRules(final Line line, final Queue<ScriptElement> created) {
		boolean success = false;
		for (Rule rule : rules) {
			if (rule.applies(line)) {
				ScriptElement consumer = rule.apply(line, this, line.getLineNumber());
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

		while (!(line.startsWith(" ") || line.startsWith("("))) {
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
			--beginOfFunction;
		}


		return line.subPart(beginOfFunction, lastIndex);
	}

	private String evaluateFunction(String functionName, String[] args, int lineNumber) {
		Function function = functions.get(functionName);
		if (function == null) {
			error("Unknown function " + functionName);
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
		return results.toArray(new String[0]);
	}

	private String spliceNextFunction(Line line) {
		int currentBracketStand = 0;
		StringBuilder currentArgument = new StringBuilder();
		for (char character : line) {
			if (character == '(') {
				++currentBracketStand;
				currentArgument.append(character);
			} else if (character == ')') {
				--currentBracketStand;
				currentArgument.append(character);
				if (currentBracketStand == 0) {
					line.remove(0, currentArgument.length());
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

		while (containsFunction(line.toString())) {
			results.add(spliceNextFunction(line));
		}

		return results.toArray(new String[0]);
	}

	private String[] getArgumentsOfFunction(Line line) {
		List<String> results = new ArrayList<>();

		StringBuilder currentArgument = new StringBuilder();
		while (!line.isEmpty()) {
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
				line.remove(0, currentArgument.length());
				currentArgument = new StringBuilder();
			}
		}

		return results.toArray(new String[0]);
	}

	private boolean isFunction(Line line) {
		if (!line.matches("[a-zA-Z0-9_][a-zA-Z0-9_-]+\\(.*")) {
			return false;
		}

		boolean foundLastClosingBracket = false;
		int bracketCount = 0;
		for (char currentChar : line) {
			if (currentChar == ')') {
				--bracketCount;
				if (bracketCount == 0) {
					foundLastClosingBracket = true;
				}
			} else if (currentChar == '(') {
				// We found a function,
				// nested within the provided line
				// now check, if we found a
				// separate function or "only"
				// another function as a parameter
				// to our function.
				if (foundLastClosingBracket) {
					return false;
				} else {
					++bracketCount;
				}
			} else if (foundLastClosingBracket && currentChar != ' ') {
				// We already found the end of our function
				// If we now find a char, that
				// is not an ' ', we should return
				// false, because we found another
				// char next to our function
				// and the provided line there for
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

	private void preEvaluation(LineParser lines) {
		for (Line line : lines) {
			int relation = relationOpenClosed(line);
			if (relation > 0) {
				error("Missing closing bracket(s): " + relation, line.getLineNumber());
			} else if (relation < 0) {
				error("To many closing bracket(s): " + -relation, line.getLineNumber());
			}
		}
	}

	private int relationOpenClosed(Line line) {
		// > 0 means more open than closed
		// < 0 means more closed than open
		return line.countCharInLine('(') - line.countCharInLine(')');
	}

	private void checkForError() throws ParsingFailedException {
		if (!running.get()) {
			throw new ParsingFailedException(faultyLines);
		}
	}

	public synchronized Script parse(String string) throws ParsingFailedException {
		return parse(LineProvider.ofString(string));
	}

	public synchronized Script parse(LineProvider lineProvider) throws ParsingFailedException {
		running.set(true);
		synchronized (lineParser) {
			lineParser.setLines(lineProvider);
		}
		preEvaluation(lineParser);
		// We check here, to ensure basic
		// stuff works (i.e. every \\( is
		// closed within the same line)
		// This might be changed, to account
		// for multi-line brackets,
		// but it is way easier that way
		checkForError();
		Script result;

		try {
			functionsLock.lock();
			ruleLock.lock();
			while(lineParser.hasNext()) {
				Line line = lineParser.getNext().duplicate();
				if(line.isBlank()) {
					continue;
				}
				try {
					if(containsFunction(line.toString())) {
						applyFunctions(line);
					}

					applyRules(line, created);
				} catch (Exception e) {
					throw new ParsingFailedException("Encountered unexpected Exception while parsing!", e, lineParser.getLinePointer());
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
				checkForError();
			}

			// Here we assume, we parsed
			// correctly (since we threw
			// no exception). But to check,
			// for any potential error in
			// the last line, we have to
			// check again, if we
			// terminated ahead of time.
			checkForError();
		} finally {
			functionsLock.unlock();
			ruleLock.unlock();
			synchronized (this) {
				internalVariables.clear();
				result = new Script(created);
				created.clear();
				currentFunctionCount = 0;
			}
			running.set(false);
		}

		return result;
	}

	public Register getParserRegister() {
		return internalVariables;
	}

	public void freezeLinePointer() {
		lineParser.freezeLinePointer();
	}

	public void setLinePointer(int to) {
		lineParser.setLinePointer(to);
	}

	public void setInternalVariable(String key, String value) {
		this.internalVariables.put(key, value);
	}

	public String getInternalVariable(String key) {
		return this.internalVariables.get(key);
	}

	public void error(String message, int lineNumber) {
		running.set(false);
		faultyLines.add(lineNumber);
		errorMessages.add(message);
		diagnosticManagerReference.get().onError(message, lineParser.getLine(lineNumber));
	}

	public void error(final String message) {
		error(message, lineParser.getLinePointer());
	}

	public void warn(String message, int lineNumber) {
		Line line = lineParser.getLine(lineNumber);
		diagnosticManagerReference.get().onWarning(message, line);
	}

	public void warn(String message) {
		warn(message, lineParser.getLinePointer());
	}

	public void notice(String message, int lineNumber) {
		Line line = lineParser.getLine(lineNumber);
		diagnosticManagerReference.get().onNotice(message, line);
	}

	public void notice(String message) {
		notice(message, lineParser.getLinePointer());
	}

	public void clearInternalVariable(String name) {
		internalVariables.remove(name);
	}

	public void insert(ScriptElement consumer) {
		created.add(consumer);
	}

	public void deleteInternalVariable(String name) {
		internalVariables.remove(name);
	}

	public void add(Function function) {
		try {
			functionsLock.lock();
			functions.put(function.getFunctionName(), function);
		} finally {
			functionsLock.unlock();
		}
	}

	public void add(Rule rule) {
		try {
			ruleLock.lock();
			this.rules.add(rule);
		} finally {
			ruleLock.unlock();
		}
	}

	public void add(Package newPackage) {
		for (Rule rule : newPackage.getRules()) {
			add(rule);
		}

		for (Function function : newPackage.getFunctions()) {
			add(function);
		}
	}

	public void addParsingFailedHandler(Consumer<ParsingFailedException> consumer) {
		errorPipeline.addFirst(consumer);
	}

	public void setDiagnosticManager(DiagnosticManager diagnosticManager) {
		Objects.requireNonNull(diagnosticManager);
		synchronized (this.diagnosticManagerReference) {
			this.diagnosticManagerReference.set(diagnosticManager);
		}
	}

	@Override
	public String toString() {
		return "Parser{" +
				"rules=" + rules +
				", internalVariables=" + internalVariables +
				", running=" + running +
				'}';
	}

}
