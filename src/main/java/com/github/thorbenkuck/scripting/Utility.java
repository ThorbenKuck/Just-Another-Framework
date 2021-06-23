package com.github.thorbenkuck.scripting;

import java.util.Arrays;
import java.util.function.Consumer;

public class Utility {

	public static ScriptElement<Register> createPrintVariable(String variableName) {
		return new ScriptElement<Register>() {
			public void accept(Register register) {
				System.out.print(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintVariable(" + variableName + ")";
			}
		};
	}

	public static ScriptElement<Register> createPrintText(String what) {

		return new ScriptElement<Register>() {
			public void accept(Register register) {
				System.out.print(what);
			}

			@Override
			public String toString() {
				return "PrintText(\"" + what + "\")";
			}
		};
	}

	public static ScriptElement<Register> createPrintLineVariable(String variableName) {
		return new ScriptElement<Register>() {
			public void accept(Register register) {
				System.out.println(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintlnVariable(" + variableName + ")";
			}
		};
	}

	public static ScriptElement<Register> createPrintLineText(String what) {
		return new ScriptElement<Register>() {
			public void accept(Register register) {
				System.out.println(what);
			}

			@Override
			public String toString() {
				return "PrintlnText(\"" + what + "\")";
			}
		};
	}

	public static ScriptElement<Register> wrapFunction(Function function, String registerAddress, String[] args) {
		if(Function.NO_RETURN_VALUE.equals(function.hintReturnValue())) {
			return new ScriptElement<Register>() {

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
			return new ScriptElement<Register>() {

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
}
