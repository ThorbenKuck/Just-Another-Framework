package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.exceptions.RuntimeExecutionException;
import com.github.thorbenkuck.scripting.parsing.Parser;

public class RequireIsSetFunction implements Function {
	@Override
	public final String getFunctionName() {
		return "require";
	}

	@Override
	public final String calculate(String[] args, Register register) {
		for(String arg : args) {
			if(!register.has(arg)) {
				throw new RuntimeExecutionException("The value of " + arg + " is never set!");
			}
		}
		return NO_RETURN_VALUE;
	}

	@Override
	public void onParse(String[] args, Parser parser, int lineNumber) {
		if(args.length == 0) {
			parser.error("At least one variable must be provided", lineNumber);
		}
	}

	@Override
	public final String hintReturnValue() {
		return NO_RETURN_VALUE;
	}
}
