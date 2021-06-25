package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.Register;

import java.io.PrintStream;

public class PrintFunction implements Function {

	private final PrintStream out;

	public PrintFunction() {
		this(System.out);
	}

	public PrintFunction(final PrintStream out) {
		this.out = out;
	}

	@Override
	public String calculate(String[] args, Register register) {
		for(String name : args) {
			IOUtils.printAccordingToType(name, register, out);
		}

		return NO_RETURN_VALUE;
	}

	@Override
	public void onParse(final String[] args, final Parser parser, final int lineNumber) {
		if(args.length == 0) {
			parser.error(getFunctionName() + " expects at least one argument");
		}
	}

	@Override
	public String getFunctionName() {
		return "print";
	}

	@Override
	public String hintReturnValue() {
		return NO_RETURN_VALUE;
	}
}
