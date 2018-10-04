package com.github.thorbenkuck.scripting;

import java.io.Serializable;
import java.util.Map;

public interface Register extends Serializable {

	static Register create() {
		return new MappingRegister();
	}

	String NULL_VALUE = "null";

	void put(String key, String value);

	String get(String key);

	void remove(String name);

	void clear();

	void adapt(Map<String, String> initialRegisterValues);
}
