package com.github.thorbenkuck.scripting;

class PrintLineFunction implements Function {

	@Override
	public String calculate(String[] args, Register register) {
		if(args.length == 0) {
			Utility.createPrintLineText("").accept(register);
			return NO_RETURN_VALUE;
		}
		else if(args.length == 1) {
			String arg = args[0];
			if(isVariable(arg)) {
				Utility.createPrintLineVariable(arg).accept(register);
			} else {
				Utility.createPrintLineText(arg).accept(register);
			}
			return NO_RETURN_VALUE;
		}

		// TODO Auslagern
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

		Utility.createPrintLineText("").accept(register);

		return NO_RETURN_VALUE;
	}

	@Override
	public String getFunctionName() {
		return "println";
	}

	private boolean isVariable(String string) {
		return !string.startsWith("\"");
	}
}
