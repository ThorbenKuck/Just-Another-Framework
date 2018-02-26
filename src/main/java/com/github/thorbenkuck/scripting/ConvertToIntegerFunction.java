package com.github.thorbenkuck.scripting;

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
			if(!Utility.isInteger(value)) {
				register.put(arg, "0");
			}
		}
		return NO_RETURN_VALUE;
	}
}
