package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.packages.Package;

import java.util.function.Consumer;

public interface Parser {

	static Parser create() {
		return new ParserImpl();
	}

	Script parse(String string) throws ParsingFailedException;

	Script parse(LineProvider lineProvider) throws ParsingFailedException;

	void freezeLinePointer();

	void setLinePointer(int to);

	void setInternalVariable(String key, String value);

	String getInternalVariable(String key);

	void add(Rule rule);

	void error(String message, int lineNumber);

	void add(Package newPackage);

	void error(String message);

	void clearInternalVariable(String name);

	void insert(Consumer<Register> consumer);

	void deleteInternalVariable(String name);

	void add(Function function);
}
