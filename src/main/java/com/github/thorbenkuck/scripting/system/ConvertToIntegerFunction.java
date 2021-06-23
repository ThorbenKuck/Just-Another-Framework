package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;

public class ConvertToIntegerFunction implements Function {

	@Override
	public String getFunctionName() {
		return "convertToInt";
	}

	@Override
	public String calculate(String[] args, Register register) {
		for(String arg : args) {
			String value = register.get(arg);
			if(value.equals(Register.NULL_VALUE)) {
				continue;
			}
			if(! isInteger(value)) {
				register.put(arg, "0");
			}
		}
		return NO_RETURN_VALUE;
	}

	@Override
	public String hintReturnValue() {
		return NO_RETURN_VALUE;
	}
}
