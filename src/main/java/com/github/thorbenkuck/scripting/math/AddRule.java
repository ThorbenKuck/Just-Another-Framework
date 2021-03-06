package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.*;

import java.util.function.Consumer;

public class AddRule implements Rule {

	private int currentRegisterValue = 0;

	@Override
	public boolean applies(final Line line) {
//		return line.matches(".*[a-zA-Z0-9][ ]*[\\+][ ]*[a-zA-Z0-9].*");
		return false;
	}

	@Override
	public ScriptElement<Register> apply(final Line line, final Parser parser, final int linePointer) {
		// Since we are lazy,
		// we just wrap the
		// add function, to
		// handle this one
		return Utility.wrapFunction(new AddFunction(), "R#add" + (currentRegisterValue++), getArgs(line, parser));
	}

	private String[] getArgs(final Line line, final Parser parser) {
		// Now this is a challenge..
		// good luck future me!
		return new String[0];
	}
}
