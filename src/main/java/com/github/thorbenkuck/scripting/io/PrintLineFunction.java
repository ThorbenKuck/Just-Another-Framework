package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;

public class PrintLineFunction implements Function {

	@Override
	public String calculate(String[] args, Register register) {
		if(args.length == 0) {
			IOUtils.printAccordingToType("", register, "\n");
			return NO_RETURN_VALUE;
		}

		for(String name : args) {
			IOUtils.printAccordingToType(name, register);
		}

		Utility.createPrintLineText("").accept(register);

		return NO_RETURN_VALUE;
	}

	@Override
	public String getFunctionName() {
		return "println";
	}

	@Override
	public String hintReturnValue() {
		return NO_RETURN_VALUE;
	}
}
