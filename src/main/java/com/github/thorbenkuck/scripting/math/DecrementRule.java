package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.exceptions.RuntimeExecutionException;

public class DecrementRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches("\\-\\-[a-zA-Z0-9]");
	}

	@Override
	public ScriptElement<Register> apply(Line line, Parser parser, int linePointer) {
		String reference = line.subPart(2, line.length()).toString();
		return new ScriptElement<Register>() {
			@Override
			public void accept(Register register) {
				if(register.get(reference).equals(Register.NULL_VALUE)) {
					throw new RuntimeExecutionException("Decrement is only applicable to variables");
				}
				String currentValue = register.get(reference);
				if(isInteger(currentValue)) {
					int intValue = asInt(currentValue);
					--intValue;
					register.put(reference, String.valueOf(intValue));
				} else if(isDouble(currentValue)) {
					double value = asDouble(currentValue);
					--value;
					register.put(reference, String.valueOf(value));
				} else {
					throw new RuntimeExecutionException("Unknown type of " + currentValue);
				}
			}

			@Override
			public String toString() {
				return "decrease " + reference;
			}
		};
	}
}
