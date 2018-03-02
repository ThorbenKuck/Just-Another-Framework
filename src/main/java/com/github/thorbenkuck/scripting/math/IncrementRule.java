package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.exceptions.ExecutionRuntimeException;

import java.util.function.Consumer;

public class IncrementRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches("\\+\\+[a-zA-Z0-9]");
	}

	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		String reference = line.subpart(2, line.length()).toString();
		return new Consumer<Register>() {
			@Override
			public void accept(Register register) {
				if(register.get(reference).equals(Register.NULL_VALUE)) {
					throw new ExecutionRuntimeException("Increment is only applicable to variables");
				}
				String currentValue = register.get(reference);
				if(Utility.isInteger(currentValue)) {
					int intValue = Utility.toInt(currentValue);
					++intValue;
					register.put(reference, String.valueOf(intValue));
				} else if(Utility.isDouble(currentValue)) {
					double value = Utility.toDouble(currentValue);
					++value;
					register.put(reference, String.valueOf(value));
				} else {
					throw new ExecutionRuntimeException("Unknown type of " + currentValue);
				}
			}

			@Override
			public String toString() {
				return "increase " + reference;
			}
		};
	}
}
