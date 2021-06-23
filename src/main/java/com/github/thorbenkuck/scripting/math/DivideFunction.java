package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;
import com.github.thorbenkuck.scripting.exceptions.RuntimeExecutionException;

import java.util.Arrays;

public class DivideFunction implements Function {

	@Override
	public String getFunctionName() {
		return "div";
	}

	@Override
	public String calculate(final String[] args, final Register register) {
		if(isDouble(args[0]) || isDouble(args[1])) {
			return fromDouble(args, register);
		} else if(isInteger(args[0]) && isInteger(args[1])) {
			return fromInt(args, register);
		}
		throw new RuntimeExecutionException("Unknown provided types: " + Arrays.asList(args));
	}

	private String fromInt(final String[] args, final Register register) {
		int one = toInt(args[0], register);
		int two = toInt(args[1], register);
		return String.valueOf((one / two));
	}

	private String fromDouble(final String[] args, final Register register) {
		double one = toDouble(args[0], register);
		double two = toDouble(args[1], register);
		return String.valueOf((one / two));
	}

	@Override
	public void onParse(final String[] args, final Parser parser, final int lineNumber) {
		// Vlt. auslagern?
		if(args.length != 2) {
			parser.error(getFunctionName() + " requires 2 arguments. Provided: " + args.length);
		}
	}
}
