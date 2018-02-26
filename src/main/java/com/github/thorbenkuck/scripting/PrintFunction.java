package com.github.thorbenkuck.scripting;

class PrintFunction implements Function {

	// TODO Pre-evaluation check for arguments

	@Override
	public String calculate(String[] args, Register register) {
		for(String name : args) {
			if (name.startsWith("\"")) {
				String withoutLeading = name.substring(1, name.length());
				String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
				Utility.createPrintText(toPrint).accept(register);
			} else if (!name.equals("")) {
				Utility.createPrintVariable(name).accept(register);
			} else {
				Utility.createPrintText("").accept(register);
			}
		}

		return NO_RETURN_VALUE;
	}

	@Override
	public String getFunctionName() {
		return "print";
	}

}
