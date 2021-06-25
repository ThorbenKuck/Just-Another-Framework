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
		return new DeleteRuleScripElement(name);
	}

	public static final class DeleteRuleScripElement implements ScriptElement {

		private final String name;

		public DeleteRuleScripElement(String name) {
			this.name = name;
		}

		@Override
		public final void accept(Register register) {
			// Blindly remove it
			register.remove(name);
		}

		@Override
		public final String describe() {
			return "Delete " + name;
		}
	}
}
