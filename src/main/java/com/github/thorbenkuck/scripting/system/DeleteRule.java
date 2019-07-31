package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;

public class DeleteRule implements Rule {
	@Override
	public boolean applies(Line line) {
		return line.matches("delete[ ]+[a-zA-Z0-9]+");
	}

	@Override
	public ScriptElement<Register> apply(Line line, Parser parser, int linePointer) {
		String name = line.subPart(line.lastIndexOf(" ") + 1).toString();
		return new ScriptElement<Register>() {
			@Override
			public void accept(Register register) {
				// Blindly remove it
				register.clear(name);
			}

			@Override
			public String toString() {
				return "Delete " + name;
			}
		};
	}
}
