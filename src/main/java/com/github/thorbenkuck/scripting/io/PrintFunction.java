package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.exceptions.ExecutionRuntimeException;

public class PrintFunction implements Function {

	// TODO Pre-evaluation check for arguments

	@Override
	public String calculate(String[] args, Register register) {
		if(args.length == 0) {
			throw new ExecutionRuntimeException("No parameters for print given!");
		}
		for(String name : args) {
			IOUtils.printAccordingToType(name, register);
		}

		return NO_RETURN_VALUE;
	}

	@Override
	public String getFunctionName() {
		return "print";
	}

	@Override
	public String hintReturnValue() {
		return NO_RETURN_VALUE;
	}
}
