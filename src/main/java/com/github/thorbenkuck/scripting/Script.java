package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.util.Map;

public interface Script {
	void run(Map<String, String> registerValues) throws ExecutionFailedException;

	void run() throws ExecutionFailedException;

	void setValue(String key, String value);
}
