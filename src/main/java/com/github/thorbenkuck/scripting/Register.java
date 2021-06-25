package com.github.thorbenkuck.scripting;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Register {

	private final Map<String, String> core;

	public static String NOT_KNOWN = "undefined";

	public Register(Map<String, String> values) {
		this.core = new HashMap<>(values);
	}

	public Register() {
		this(new HashMap<>());
	}

	public void put(String key, String value) {
		core.put(key, value);
	}

	public String get(String key) {
		return core.getOrDefault(key, NOT_KNOWN);
	}

	public void ifPresent(String key, Consumer<String> consumer) {
		if(has(key)) {
			consumer.accept(get(key));
		}
	}

	public boolean has(String key) {
		return core.containsKey(key);
	}

	public void remove(String name) {
		core.remove(name);
	}

	public void clear() {
		core.clear();
	}

	public void adapt(Map<String, String> initialRegisterValues) {
		initialRegisterValues.forEach(core::putIfAbsent);
	}

	@Override
	public String toString() {
		return core.toString();
	}
}
