package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

class IntegerIncrementRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches("\\+\\+[a-zA-Z0-9]");
	}

	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		String variable = line.subpart(2, line.length()).toString();
		return new Consumer<Register>() {
			@Override
			public void accept(Register register) {
				String currentValue = register.get(variable);
				int intValue = Integer.parseInt(currentValue);
				++intValue;
				register.put(variable, String.valueOf(intValue));
			}

			@Override
			public String toString() {
				return "increase " + variable;
			}
		};
	}
}
