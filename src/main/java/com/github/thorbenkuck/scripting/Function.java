package com.github.thorbenkuck.scripting;

public interface Function {

	String NO_RETURN_VALUE = "void";

	java.util.function.Function<String, String> asStringValue = string -> "\"" + string + "\"";

	java.util.function.Function<String, Boolean> isVariable = string -> !string.startsWith("\"") && !string.endsWith("\"");

	static Function print() {
		return new PrintFunction();
	}

	static Function println() {
		return new PrintLineFunction();
	}

	static Function require() {
		return new RequireIsSetFunction();
	}

	static void applyDefaults(Parser parser) {
		parser.addFunction(Function.convertToInt());
		parser.addFunction(Function.require());
		parser.addFunction(Function.print());
		parser.addFunction(Function.println());
	}

	static Function convertToInt() {
		return new ConvertToIntegerFunction();
	}

	String getFunctionName();

	default String calculate(String[] args, Register register) {
		return NO_RETURN_VALUE;
	}

	default void onParse(String[] args, Parser parser, int lineNumber) {

	}

}
