package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;

class IOUtils {

	static void printAccordingToType(String s, Register register) {
		printAccordingToType(s, register, "");
	}

	static void printAccordingToType(String s, Register register, String suffix) {
		if (Function.isVariable.apply(s,register)) {
			Utility.createPrintVariable(s).accept(register);
		} else if(Function.isString.apply(s)) {
			String withoutLeading = s.substring(1, s.length());
			String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
			Utility.createPrintText(toPrint).accept(register);
		} else {
			Utility.createPrintText(s).accept(register);
		}
		System.out.print(suffix);
	}
}
