package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.VariableEvaluation;
import com.github.thorbenkuck.scripting.exceptions.RuntimeExecutionException;

import java.io.PrintStream;

class IOUtils {

	static void printAccordingToType(String s, Register register, PrintStream printStream) throws RuntimeExecutionException {
		printAccordingToType(s, register, printStream, "");
	}

	static void printAccordingToType(String s, Register register, PrintStream printStream, String suffix) throws RuntimeExecutionException {
		if (register.has(s)) {
			printStream.print(register.get(s));
		} else if(VariableEvaluation.isAString(s)) {
			String withoutLeading = s.substring(1);
			String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
			printStream.print(toPrint);
		} else {
			printStream.print("nil");
		}
		printStream.print(suffix);
	}

	static void newLine(PrintStream printStream) {
		printStream.print(System.lineSeparator());
	}
}
