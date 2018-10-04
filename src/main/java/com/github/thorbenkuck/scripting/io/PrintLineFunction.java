package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Register;

import java.io.PrintStream;

public class PrintLineFunction implements Function {

	private final PrintStream out;

	public PrintLineFunction() {
		this(System.out);
	}

	public PrintLineFunction(final PrintStream out) {
		this.out = out;
	}

	@Override
	public String calculate(String[] args, Register register) {
		if (out == null) {
			return NO_RETURN_VALUE;
		}
		if(args.length == 0) {
			IOUtils.printAccordingToType("", register, out, "\n");
			return NO_RETURN_VALUE;
		}

		for(String name : args) {
			IOUtils.printAccordingToType(name, register, out);
		}

		IOUtils.printAccordingToType("", register, out, "\n");

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
