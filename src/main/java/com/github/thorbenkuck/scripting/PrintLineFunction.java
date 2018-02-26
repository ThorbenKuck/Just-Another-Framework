package com.github.thorbenkuck.scripting;

public class PrintLineFunction implements Function {

	@Override
	public String calculate(String[] args, Register register) {
		for(String name : args) {
			if (name.startsWith("\"")) {
				String withoutLeading = name.substring(1, name.length());
				String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
				Utility.createPrintLineText(toPrint).accept(register);
			} else if (!name.equals("")) {
				Utility.createPrintLineVariable(name).accept(register);
			} else {
				Utility.createPrintLineText("").accept(register);
			}
		}

		return NO_RETURN_VALUE;
	}

	@Override
	public String getFunctionName() {
		return "println";
	}

}
