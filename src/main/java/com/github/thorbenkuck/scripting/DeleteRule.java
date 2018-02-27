package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

class DeleteRule implements Rule {
	@Override
	public boolean applies(Line line) {
		return line.matches("delete[ ]+[a-zA-Z0-9]+");
	}

	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		String name = line.subpart(line.lastIndexOf(" ") + 1).toString();
		return new Consumer<Register>() {
			@Override
			public void accept(Register register) {
				// Blindly remove it
				register.remove(name);
			}

			@Override
			public String toString() {
				return "Delete " + name;
			}
		};
	}
}
