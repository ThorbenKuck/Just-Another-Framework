package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.components.Rule;
import com.github.thorbenkuck.scripting.exceptions.RuntimeExecutionException;
import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.ScriptElement;

public class VariableDefinitionRule implements Rule {

	@Override
	public boolean applies(Line line) {
		return line.matches(".+[ ]*=[ ]*.+");
	}

	@Override
	public ScriptElement apply(Line line, Parser parser, int linePointer) {
		String name = parseVariableName(line.duplicate());
		String value = parseVariableValue(line.duplicate());
		return new ScriptElement() {
			@Override
			public void accept(Register register) {
				if(!register.has(name)) {
					throw new RuntimeExecutionException(name + " is not defined");
				} else {
					if(VariableEvaluation.isAVariable(value, register)) {
						if(!register.has(value)) {
							throw new RuntimeExecutionException("Tried to set null variable!");
						} else {
							register.put(name, register.get(value));
						}
					} else {
						register.put(name, value);
					}
				}
			}

			@Override
			public String toString() {
				return "SetVariable(" + name + " to " + value + ")";
			}
		};
	}

	private String parseVariableValue(final Line input) {
		String potentialValue = input.toString().substring(input.indexOf("=") + 1, input.length());
		Line potentialValueLine = Line.create(potentialValue, - 1);
		while (potentialValueLine.getAt(0) == ' ') {
			potentialValueLine.remove(0);
		}

		StringBuilder result = new StringBuilder();

		for (int i = 0; i < potentialValueLine.length(); i++) {
			if (potentialValueLine.getAt(i) == ' ') {
				break;
			}

			result.append(potentialValueLine.getAt(i));
		}

		potentialValueLine.clear();

		return result.toString();
	}

	private String parseVariableName(Line input) {
		StringBuilder line = new StringBuilder(input.toString());
		String lineString = line.toString();

		int equalsIndex = lineString.indexOf("=");
		String potentialName = line.substring(0, equalsIndex);
		Line potentialNameLine = Line.create(potentialName, - 1);
		while (potentialNameLine.getAt(equalsIndex - 1) == ' ') {
			potentialNameLine.remove(equalsIndex - 1);
			-- equalsIndex;
		}

		StringBuilder result = new StringBuilder();

		int startingIndex = equalsIndex - 1;
		for (int i = startingIndex; i >= 0; i--) {
			if (potentialNameLine.getAt(i) == ' ') {
				break;
			}

			result.append(potentialNameLine.getAt(i));
		}

		result.reverse();
		potentialNameLine.clear();

		return result.toString();
	}
}
