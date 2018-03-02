package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;

import java.util.Arrays;

public class AddFunction implements Function {
	@Override
	public String getFunctionName() {
		return "add";
	}

	@Override
	public String calculate(final String[] args, final Register register) {
		int count = 0;

		for (String arg : args) {
			String value;
			if (isVariable.apply(arg, register)) {
				value = register.get(arg);
			} else {
				value = arg;
			}

			if (Utility.isInteger(value)) {
				count += Integer.parseInt(value);
			} else {
				System.out.println("Unknown type provided to subtract: " + arg);
			}
		}

		return String.valueOf(count);
	}

	@Override
	public void onParse(final String[] args, final Parser parser, final int lineNumber) {
		if (args.length <= 1) {
			parser.error("at least 2 numbers have to be provided.. Provided: " + Arrays.toString(args), lineNumber);
		}
	}
}
