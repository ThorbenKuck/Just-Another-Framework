package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Utility;

import java.io.PrintStream;

class IOUtils {

	static void printAccordingToType(String s, Register register, PrintStream printStream) {
		printAccordingToType(s, register, printStream, "");
	}

	static void printAccordingToType(String s, Register register, PrintStream printStream, String suffix) {
		if (! register.get(s).equals(Register.NULL_VALUE)) {
			printStream.print(register.get(s));
		} else if(Function.isString.apply(s)) {
			String withoutLeading = s.substring(1, s.length());
			String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
			printStream.print(toPrint);
		} else {
			printStream.print(s);
		}
		System.out.print(suffix);
	}
}
