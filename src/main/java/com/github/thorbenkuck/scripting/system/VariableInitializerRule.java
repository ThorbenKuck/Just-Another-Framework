package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.components.Rule;
import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.ScriptElement;

public class VariableInitializerRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches("var [a-zA-Z0-9]+.*");
	}

	@Override
	public ScriptElement apply(Line line, Parser parser, int linePointer) {
		StringBuilder stringBuilder = new StringBuilder(line.toString());
		String name = parseVariableName(stringBuilder);
		if(!parser.getInternalVariable(name).equals(Register.NOT_KNOWN)) {
			parser.error("double definition of variable " + name, linePointer);
		}
		parser.setInternalVariable(name, "undefined");
		line.remove(0, 3);
		return new ScriptElement() {
			@Override
			public void accept(Register register) {
				if(!register.has(name)) {
					register.put(name, "undefined");
				} else {
					parser.error(name + " is already defined!", line.getLineNumber());
				}
			}

			@Override
			public String toString() {
				return "InitializeVariable(" + name + ")";
			}
		};
	}

	private String parseVariableName(StringBuilder line) {
		if (!(line.indexOf("var ") == 0)) {
			return "null";
		}

		line.delete(0, "var ".length());

		String lineString = line.toString();

		String name = line.substring(0, lineString.contains(" ") ? lineString.indexOf(" ") : lineString.length());
		line.delete(0, name.length());

		return name;
	}

}
