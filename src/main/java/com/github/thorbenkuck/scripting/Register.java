package com.github.thorbenkuck.scripting;

import java.io.Serializable;
import java.util.Map;

public interface Register extends Serializable {

	static Register create() {
		return new MappingRegister();
	}

	String NULL_VALUE = "null";

	Byte[] NULL_BYTES = new Byte[0];

	void put(String key, String value);

	void put(Byte key, Byte value);

	void put(Byte key, Byte[] value);

	String get(String key);

	Byte[] get(Byte key);

	void clear(String name);

	void clear(Byte key);

	void clear();

	void adapt(Map<String, String> initialRegisterValues);
}
