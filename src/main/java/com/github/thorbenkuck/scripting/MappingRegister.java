package com.github.thorbenkuck.scripting;

import java.util.HashMap;
import java.util.Map;

final class MappingRegister implements Register {

	private final Map<String, String> core = new HashMap<>();
	private final Map<Byte, Byte[]> byteMap = new HashMap<>();

	@Override
	public void put(String key, String value) {
		core.put(key, value);
	}

	@Override
	public void put(Byte key, Byte value) {
		put(key, new Byte[]{value});
	}

	@Override
	public void put(Byte key, Byte[] value) {
		byteMap.put(key, value);
	}

	@Override
	public String get(String key) {
		return core.getOrDefault(key, NULL_VALUE);
	}

	@Override
	public Byte[] get(Byte key) {
		return byteMap.getOrDefault(key, NULL_BYTES);
	}

	@Override
	public void clear(String name) {
		core.remove(name);
	}

	@Override
	public void clear(Byte key) {
		byteMap.remove(key);
	}

	@Override
	public void clear() {
		core.clear();
		byteMap.clear();
	}

	@Override
	public String toString() {
		return "MappingRegister{" +
				"core=" + core +
				", byteMap=" + byteMap +
				'}';
	}

	@Override
	public void adapt(Map<String, String> initialRegisterValues) {
		core.putAll(initialRegisterValues);
	}
}
