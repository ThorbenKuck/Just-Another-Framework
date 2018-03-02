package com.github.thorbenkuck.scripting;

public interface Function {

	String NO_RETURN_VALUE = "void";

	java.util.function.Function<String, String> asStringValue = string -> "\"" + string + "\"";

	java.util.function.Function<String, Boolean> isString = string -> string.startsWith("\"") && string.endsWith("\"");

	java.util.function.BiFunction<String, Register, Boolean> isVariable = (string, register) -> ! isString.apply(string) && ! register.get(string).equals(Register.NULL_VALUE);

	String getFunctionName();

	String calculate(String[] args, Register register);

	default void onParse(String[] args, Parser parser, int lineNumber) {
	}

	default String hintReturnValue() { return ""; }
}
