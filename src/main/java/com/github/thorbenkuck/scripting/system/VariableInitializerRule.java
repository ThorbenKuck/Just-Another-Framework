package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.Line;
import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.Rule;

import java.util.function.Consumer;

public class VariableInitializerRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches("var [a-zA-Z0-9]+.*");
	}

	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		StringBuilder stringBuilder = new StringBuilder(line.toString());
		String name = parseVariableName(stringBuilder);
		if(!parser.getInternalVariable(name).equals(Register.NULL_VALUE)) {
			parser.error("double definition of variable " + name, linePointer);
		}
		parser.setInternalVariable(name, "undefined");
		line.remove(0, 3);
		return new Consumer<Register>() {
			@Override
			public void accept(Register register) {
				if(Register.NULL_VALUE.equals(register.get(name))) {
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
