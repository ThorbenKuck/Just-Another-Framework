package com.github.thorbenkuck.scripting;

import java.util.HashMap;
import java.util.Map;

class MappingRegister implements Register {

	private final Map<String, String> core;

	public MappingRegister(Map<String, String> values) {
		this.core = new HashMap<>(values);
	}

	public MappingRegister() {
		this(new HashMap<>());
	}

	@Override
	public void put(String key, String value) {
		core.put(key, value);
	}

	@Override
	public String get(String key) {
		if(VariableEvaluation.isAnInteger(key)) {
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
		initialRegisterValues.forEach(core::putIfAbsent);
	}
}
