package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.parsing.Parser;

public class SubtractFunction implements Function {

	private String getValue(String s, Register register) {
		if (isVariable(s, register)) {
			return register.get(s);
		} else if (isInteger(s)) {
			return s;
		}

		return "0";
	}

	@Override
	public String getFunctionName() {
		return "subtract";
	}

	@Override
	public String calculate(final String[] args, final Register register) {
		// We ca safely assume that
		// those elements exist,
		// because we check at parsing
		// time. We do have to first
		// get the 0th element, so that
		// we subtract correctly.
		int sum;
		String value = getValue(args[0], register);
		if (isInteger(value, register)) {
			sum = toInt(value, register);
		} else {
			sum = 0;
			System.out.println("Unknown type provided to subtract: " + value);
		}
		args[0] = "0";

		for (String arg : args) {
			value = getValue(arg, register);

			if (isInteger(value, register)) {
				sum -= toInt(value, register);
			} else {
				System.out.println("Unknown type provided to subtract: " + arg);
			}
		}

		return String.valueOf(sum);
	}

	@Override
	public void onParse(final String[] args, final Parser parser, final int lineNumber) {
		if (args.length <= 1) {
			parser.error("at least 2 numbers have to be provided.. Provided: " + args.length, lineNumber);
		}
	}
}
