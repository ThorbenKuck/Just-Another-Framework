package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

class LoopStartRule implements Rule {

	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		String rawArgs = line.toString();
		String withoutIdentifier = rawArgs.substring(5, rawArgs.length());
		StringBuilder stringBuilder = new StringBuilder(withoutIdentifier);
		String name = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
		stringBuilder.delete(0, name.length() + 1);
		String initial = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
		stringBuilder.delete(0, name.length() + 1);
		String finale = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
		stringBuilder.delete(0, name.length() + 1);
		if (!Register.NULL_VALUE.equals(parser.getInternalVariable(name))) {
			int value = Integer.parseInt(parser.getInternalVariable(name));
			++value;
			String stringedValue = String.valueOf(value);
			int finalValue = Integer.parseInt(finale);
			if (value > finalValue) {
				parser.setLinePointer(Integer.parseInt(parser.getInternalVariable("loopEnd" + name)));
				parser.deleteInternalVariable(name);
				parser.clearInternalVariable("loop" + name);
				parser.clearInternalVariable("loopEnd" + name);
				return new Consumer<Register>() {
					@Override
					public void accept(Register register) {
						register.remove(name);
					}

					@Override
					public String toString() {
						return "Loop Finalizer. Removing " + name;
					}
				};
			} else {
				parser.setInternalVariable(name, stringedValue);
				return new Consumer<Register>() {
					@Override
					public void accept(Register register) {
						int count = Integer.parseInt(register.get(name));
						++count;
						register.put(name, String.valueOf(count));

					}

					@Override
					public String toString() {
						return "Increase Loop-Count(" + name + ")";
					}
				};
			}
		} else {
			parser.setInternalVariable(name, initial);
			parser.setInternalVariable("loop" + name, String.valueOf(linePointer - 1));
			return new Consumer<Register>() {
				@Override
				public void accept(Register register) {
					register.put(name, initial);
				}

				@Override
				public String toString() {
					return "Loop initializer";
				}
			};
		}
	}

	@Override
	public boolean applies(Line line) {
		// return line.startsWith("loop [a-bA-B0-9]+ [0-9]+ : [0-9]+");
		return line.startsWith("loop");
	}

}
