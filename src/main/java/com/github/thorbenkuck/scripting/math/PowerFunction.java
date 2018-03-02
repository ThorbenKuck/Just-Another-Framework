package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;
import com.github.thorbenkuck.scripting.exceptions.ExecutionRuntimeException;

import java.util.Arrays;

public class PowerFunction implements Function {
	@Override
	public String getFunctionName() {
		return "pow";
	}

	@Override
	public String calculate(final String[] args, final Register register) {
		// Todo: maybe also allow doubles? Dunno..
		if(!Utility.isInteger(args[0], register) || !Utility.isInteger(args[0], register)) {
			throw new ExecutionRuntimeException(getFunctionName() + " requires 2 integer-values. Given: " + Arrays.toString(args));
		}
		int number = Utility.toInt(args[0], register);
		int power = Utility.toInt(args[1], register);
		return String.valueOf(Math.pow(number, power));
	}

	@Override
	public void onParse(final String[] args, final Parser parser, final int lineNumber) {
		if(args.length != 2) {
			parser.error(getFunctionName() + " expects to parameters. Given: " + args.length, lineNumber);
		}
	}
}
