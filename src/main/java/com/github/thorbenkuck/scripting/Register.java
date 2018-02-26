package com.github.thorbenkuck.scripting;

import java.util.HashMap;
import java.util.Map;

public class Register {

	private final Map<String, String> core = new HashMap<>();

	public void put(String key, String value) {
		core.put(key, value);
	}

	public String get(String key) {
		return core.getOrDefault(key, "null");
	}

	public void remove(String name) {
		core.remove(name);
	}

	public void clear() {
		core.clear();
	}
}
