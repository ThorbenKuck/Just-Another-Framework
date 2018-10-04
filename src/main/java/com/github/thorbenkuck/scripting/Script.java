package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

public interface Script extends Serializable {
	void addInstruction(ScriptElement<Register> instruction);

	void run(Map<String, String> registerValues) throws ExecutionFailedException;

	void run() throws ExecutionFailedException;

	void setValue(String key, String value);

	int countInstructions();

	void setName(String string);

	LocalDateTime getTimeOfCreation();

	String getName();
}
