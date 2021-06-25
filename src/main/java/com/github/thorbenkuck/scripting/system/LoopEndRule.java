package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.components.Rule;
import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.ScriptElement;

public class LoopEndRule implements Rule {

	@Override
	public ScriptElement apply(Line line, Parser parser, int linePointer) {
		String rawArgs = line.toString();
		String variableName = rawArgs.substring(8);

		Register register = parser.getParserRegister();

		if (!register.has("loopEnd" + variableName)) {
			register.put("loopEnd" + variableName, String.valueOf(linePointer));
		}
		if (register.has(variableName)) {
			parser.setLinePointer(Integer.parseInt(register.get("loop" + variableName)));
		}
		return null;
	}

	@Override
	public boolean applies(Line line) {
		return line.matches("endLoop[ ][a-zA-Z0-9]+");
	}

}
