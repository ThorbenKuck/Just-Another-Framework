package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;

public class SubtractFunction implements Function {

	private String getValue(String s, Register register) {
		if (isVariable.apply(s, register)) {
			return register.get(s);
		} else if (Utility.isInteger(s)) {
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
		if (Utility.isInteger(value)) {
			sum = Integer.parseInt(value);
		} else {
			sum = 0;
			System.out.println("Unknown type provided to subtract: " + value);
		}
		args[0] = "0";

		for (String arg : args) {
			value = getValue(arg, register);

			if (Utility.isInteger(value)) {
				sum -= Integer.parseInt(value);
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
