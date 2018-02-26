package com.github.thorbenkuck.scripting;

import java.util.HashMap;
import java.util.Map;

public class Register {

	private final Map<String, String> core = new HashMap<>();
	public static final String NULL_VALUE = "null";

	public void put(String key, String value) {
		core.put(key, value);
	}

	public String get(String key) {
		if(Utility.isInteger(key)) {
			return key;
		}
		return core.getOrDefault(key, NULL_VALUE);
	}

	public void remove(String name) {
		core.remove(name);
	}

	public void clear() {
		core.clear();
	}

	@Override
	public String toString() {
		return core.toString();
	}

	public void adapt(Map<String, String> initialRegisterValues) {
		core.putAll(initialRegisterValues);
	}
}
