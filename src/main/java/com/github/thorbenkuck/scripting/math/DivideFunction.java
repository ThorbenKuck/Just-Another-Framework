package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;
import com.github.thorbenkuck.scripting.exceptions.ExecutionRuntimeException;

import java.util.Arrays;

public class DivideFunction implements Function {

	@Override
	public String getFunctionName() {
		return "div";
	}

	@Override
	public String calculate(final String[] args, final Register register) {
		if(Utility.isDouble(args[0]) || Utility.isDouble(args[1])) {
			return fromDouble(args, register);
		} else if(Utility.isInteger(args[0]) && Utility.isInteger(args[1])) {
			return fromInt(args, register);
		}
		throw new ExecutionRuntimeException("Unknown provided types: " + Arrays.asList(args));
	}

	private String fromInt(final String[] args, final Register register) {
		int one = Utility.toInt(args[0], register);
		int two = Utility.toInt(args[1], register);
		return String.valueOf((one / two));
	}

	private String fromDouble(final String[] args, final Register register) {
		double one = Utility.toDouble(args[0], register);
		double two = Utility.toDouble(args[1], register);
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
