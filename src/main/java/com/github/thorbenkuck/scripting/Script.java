package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.util.Map;
import java.util.function.Consumer;

public interface Script {
	void addInstruction(ScriptElement<Register> instruction);

	void run(Map<String, String> registerValues) throws ExecutionFailedException;

	void run() throws ExecutionFailedException;

	void setValue(String key, String value);

	int countInstructions();

	void setName(String string);

	String getName();
}
