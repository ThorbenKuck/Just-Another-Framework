package com.github.thorbenkuck.scripting;

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

	public static boolean isInteger(String s) {
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i), 10) < 0) return false;
		}
		return true;

	}

}
