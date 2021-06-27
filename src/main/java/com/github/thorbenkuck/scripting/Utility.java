package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.script.ScriptElement;

import java.util.Arrays;

public class Utility {

	public static ScriptElement createPrintVariable(String variableName) {
		return new ScriptElement() {
			public void accept(Register register) {
				System.out.print(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintVariable(" + variableName + ")";
			}
		};
	}

	public static ScriptElement createPrintText(String what) {

		return new ScriptElement() {
			public void accept(Register register) {
				System.out.print(what);
			}

			@Override
			public String toString() {
				return "PrintText(\"" + what + "\")";
			}
		};
	}

	public static ScriptElement createPrintLineVariable(String variableName) {
		return new ScriptElement() {
			public void accept(Register register) {
				System.out.println(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintlnVariable(" + variableName + ")";
			}
		};
	}

	public static ScriptElement createPrintLineText(String what) {
		return new ScriptElement() {
			public void accept(Register register) {
				System.out.println(what);
			}

			@Override
			public String toString() {
				return "PrintlnText(\"" + what + "\")";
			}
		};
	}

	public static ScriptElement wrapFunction(Function function, String registerAddress, String[] args) {
		if(Function.NO_RETURN_VALUE.equals(function.hintReturnValue())) {
			return new ScriptElement() {

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
			return new ScriptElement() {

				@Override
				public void accept(Register register) {
					register.put(registerAddress, function.calculate(args, register));
				}

				@Override
				public String toString() {
					return function.getFunctionName() + "(" + Arrays.toString(args) + ") => " + registerAddress;
				}
			};
		}
	}
}
