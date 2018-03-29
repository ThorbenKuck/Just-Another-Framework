package com.github.thorbenkuck.scripting;

@FunctionalInterface
public interface ScriptElement<T> {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	void accept(T t);

	default String serialize() {
		throw new UnsupportedOperationException("Serialization");
	}

	default String deserialize() {
		throw new UnsupportedOperationException("Deserialization");
	}
}
