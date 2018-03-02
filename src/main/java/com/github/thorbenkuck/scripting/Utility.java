package com.github.thorbenkuck.scripting;

import java.util.Arrays;
import java.util.function.Consumer;

public class Utility {

	public static Consumer<Register> createPrintVariable(String variableName) {
		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.print(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintVariable(" + variableName + ")";
			}
		};
	}

	public static Consumer<Register> createPrintText(String what) {

		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.print(what);
			}

			@Override
			public String toString() {
				return "PrintText(\"" + what + "\")";
			}
		};
	}

	public static Consumer<Register> createPrintLineVariable(String variableName) {
		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.println(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintlnVariable(" + variableName + ")";
			}
		};
	}

	public static Consumer<Register> createPrintLineText(String what) {
		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.println(what);
			}

			@Override
			public String toString() {
				return "PrintlnText(\"" + what + "\")";
			}
		};
	}

	public static Consumer<Register> wrapFunction(Function function, String registerAddress, String[] args) {
		if(Function.NO_RETURN_VALUE.equals(function.hintReturnValue())) {
			return new Consumer<Register>() {

				@Override
				public void accept(Register register) {
					function.calculate(args, register);
				}

				@Override
				public String toString() {
					return function.getFunctionName() + "(" + Arrays.toString(args) + ")";
				}
			};
		} else {
			return new Consumer<Register>() {

				@Override
				public void accept(Register register) {
					register.put(registerAddress, function.calculate(args, register));
				}

				@Override
				public String toString() {
					return "Set register " + registerAddress + " to " + function.getFunctionName() + "(" + Arrays.toString(args) + ")";
				}
			};
		}
	}

	public static boolean isInteger(String s, Register register) {
		return isInteger(s) || isInteger(register.get(s));
	}

	public static boolean isInteger(String s, int radix) {
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i), radix) < 0) return false;
		}
		return true;
	}

	public static boolean isInteger(String s) {
		return isInteger(s, 10);
	}

	public static boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static double toDouble(String s) {
		return toDouble(s, 0.0);
	}

	public static double toDouble(String s, double defaultValue) {
		if(!isDouble(s)) {
			return defaultValue;
		}
		return Double.parseDouble(s);
	}

	public static double toDouble(String s, Register register) {
		return toDouble(s, register, 0.0);
	}

	public static double toDouble(String s, Register register, double defaultValue) {
		if(!isDouble(s)) {
			if(isDouble(register.get(s))) {
				return toDouble(register.get(s));
			}
			if(isInteger(s, register)) {
				return toInt(s, register);
			}

			return defaultValue;
		}

		return toDouble(s);
	}

	public static int toInt(String s) {
		return toInt(s, 0);
	}

	public static int toInt(String s, int defaultValue) {
		if(!isInteger(s)) return defaultValue;

		return Integer.parseInt(s);
	}

	public static int toInt(String s, Register register) {
		return toInt(s, register, 0);
	}

	public static int toInt(String s, Register register, int defaultValue) {
		if(!isInteger(s)) {
			if(isInteger(s, register)) {
				return toInt(register.get(s));
			}
			return defaultValue;
		}

		return toInt(s);
	}
}
