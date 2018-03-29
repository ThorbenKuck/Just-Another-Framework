package com.github.thorbenkuck.scripting;

import java.util.HashMap;
import java.util.Map;

class MappingRegister implements Register {

	private final Map<String, String> core = new HashMap<>();

	@Override
	public void put(String key, String value) {
		core.put(key, value);
	}

	@Override
	public String get(String key) {
		if(Utility.isInteger(key)) {
			return key;
		}
		return core.getOrDefault(key, NULL_VALUE);
	}

	@Override
	public void remove(String name) {
		core.remove(name);
	}

	@Override
	public void clear() {
		core.clear();
	}

	@Override
	public String toString() {
		return core.toString();
	}

	@Override
	public void adapt(Map<String, String> initialRegisterValues) {
		core.putAll(initialRegisterValues);
	}
}
