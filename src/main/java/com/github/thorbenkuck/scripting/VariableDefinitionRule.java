package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionRuntimeException;

import java.util.function.Consumer;

class VariableDefinitionRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches("[a-zA-Z0-9]+[ ]*=[ ]*[a-zA-Z0-9]+");
	}

	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		line.trimWhiteSpaces();
		StringBuilder stringBuilder = new StringBuilder(line.toString());
		String name = parseVariableName(stringBuilder);
		return new Consumer<Register>() {
			@Override
			public void accept(Register register) {
				if(Register.NULL_VALUE.equals(register.get(name))) {
					parser.error(name + " is not defined", line.getLineNumber());
				} else {
					String value = stringBuilder.toString();
					if(Function.isVariable.apply(value)) {
						String savedValue = register.get(value);
						if(Register.NULL_VALUE.equals(savedValue)) {
							throw new ExecutionRuntimeException("Tried to set null variable!");
						} else {
							register.put(name, savedValue);
						}
					} else {
						register.put(name, value);
					}
				}
			}

			@Override
			public String toString() {
				return "SetVariable(" + name + " to " + stringBuilder.toString() + ")";
			}
		};
	}

	private String parseVariableName(StringBuilder line) {
		String lineString = line.toString();

		String name = line.substring(0, lineString.indexOf("="));
		line.delete(0, name.length() + 1);

		return name;
	}

}
