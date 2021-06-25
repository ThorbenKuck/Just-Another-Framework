package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.components.Rule;
import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.ScriptElement;

public class DeleteRule implements Rule {
	@Override
	public boolean applies(Line line) {
		return line.matches("delete[ ]+[a-zA-Z0-9]+");
	}

	@Override
	public ScriptElement apply(Line line, Parser parser, int linePointer) {
		String name = line.subPart(line.lastIndexOf(" ") + 1).toString();
		return new ScriptElement() {
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
