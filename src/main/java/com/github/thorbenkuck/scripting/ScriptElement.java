package com.github.thorbenkuck.scripting;

import java.io.Serializable;

@FunctionalInterface
public interface ScriptElement<T> extends Serializable {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 */
	void accept(T t);
}
