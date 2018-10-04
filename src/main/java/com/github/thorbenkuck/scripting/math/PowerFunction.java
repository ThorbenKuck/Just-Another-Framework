package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;
import com.github.thorbenkuck.scripting.exceptions.RuntimeExecutionException;

import java.util.Arrays;

public class PowerFunction implements Function {
	@Override
	public String getFunctionName() {
		return "pow";
	}

	@Override
	public String calculate(final String[] args, final Register register) {
		// Todo: maybe also allow doubles? Dunno..
		if (Utility.isDouble(args[0], register) || Utility.isDouble(args[1], register)) {
			double number = Utility.toDouble(args[0], register);
			double power = Utility.toDouble(args[1], register);
			return String.valueOf(Math.pow(number, power));
		}

		if (Utility.isInteger(args[0], register) && Utility.isInteger(args[1], register)) {
			int number = Utility.toInt(args[0], register);
			int power = Utility.toInt(args[1], register);
			return String.valueOf(Math.pow(number, power));
		}

		throw new RuntimeExecutionException(getFunctionName() + " requires 2 numbers. Given: " + Arrays.toString(args));
	}

	@Override
	public void onParse(final String[] args, final Parser parser, final int lineNumber) {
		if(args.length != 2) {
			parser.error(getFunctionName() + " expects to parameters. Given: " + args.length, lineNumber);
		}
	}
}
