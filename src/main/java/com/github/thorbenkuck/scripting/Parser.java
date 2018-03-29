package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.packages.Package;

import java.util.function.Consumer;

public interface Parser {

	static Parser create() {
		return new ParserImpl();
	}

	static Parser create(DiagnosticManager diagnosticManager) {
		return new ParserImpl(diagnosticManager);
	}

	Script parse(String string) throws ParsingFailedException;

	Script parse(LineProvider lineProvider) throws ParsingFailedException;

	Register getParserRegister();

	void freezeLinePointer();

	void setLinePointer(int to);

	@Deprecated
	void setInternalVariable(String key, String value);

	@Deprecated
	String getInternalVariable(String key);

	void add(Rule rule);

	void error(String message, int lineNumber);

	void error(String message);

	void warn(String message, int lineNumber);

	void warn(String message);

	void notice(String message, int lineNumber);

	void notice(String message);

	void add(Package newPackage);

	@Deprecated
	void clearInternalVariable(String name);

	void insert(ScriptElement<Register> consumer);

	void deleteInternalVariable(String name);

	void add(Function function);

	void addParsingFailedHandler(Consumer<ParsingFailedException> consumer);

	void setDiagnosticManager(DiagnosticManager diagnosticManager);
}
