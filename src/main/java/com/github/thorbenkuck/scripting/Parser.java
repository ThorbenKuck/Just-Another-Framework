package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;

import java.util.function.Consumer;

public interface Parser {

	static Parser create() {
		return new ParserImpl();
	}

	Script parse(String string) throws ParsingFailedException;

	void setLinePointer(int to);

	void setInternalVariable(String key, String value);

	String getInternalVariable(String key);

	void addRule(Rule rule);

	void error(String s, int lineNumber);

	void clearInternalVariable(String name);

	void insert(Consumer<Register> consumer);

	void deleteInternalVariable(String name);

	void addFunction(Function function);
}
