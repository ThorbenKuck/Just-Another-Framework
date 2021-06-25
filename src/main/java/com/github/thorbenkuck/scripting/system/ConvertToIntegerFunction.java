package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.Register;

public class ConvertToIntegerFunction implements Function {

	@Override
	public String getFunctionName() {
		return "convertToInt";
	}

	@Override
	public String calculate(String[] args, Register register) {
		for(String arg : args) {
			if(!register.has(arg)) {
				continue;
			}
			String value = register.get(arg);
			if(!isInteger(value)) {
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
