package com.github.thorbenkuck.scripting.script;

import com.github.thorbenkuck.scripting.Register;

import java.io.Serializable;

@FunctionalInterface
public interface ScriptElement extends Serializable {

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param register the {@link Register} of the current script context.
	 */
	void accept(Register register);

	default String describe() {
		return toString();
	}
}
