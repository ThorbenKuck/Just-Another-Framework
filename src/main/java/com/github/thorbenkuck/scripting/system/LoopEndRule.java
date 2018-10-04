package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;

public class LoopEndRule implements Rule {

	@Override
	public ScriptElement<Register> apply(Line line, Parser parser, int linePointer) {
		String rawArgs = line.toString();
		String variableName = rawArgs.substring(8, rawArgs.length());

		Register register = parser.getParserRegister();

		if (Register.NULL_VALUE.equals(register.get("loopEnd" + variableName))) {
			register.put("loopEnd" + variableName, String.valueOf(linePointer));
		}
		if (!Register.NULL_VALUE.equals(register.get(variableName))) {
			parser.setLinePointer(Integer.parseInt(register.get("loop" + variableName)));
		}
		return null;
	}

	@Override
	public boolean applies(Line line) {
		return line.matches("endLoop[ ][a-zA-Z0-9]+");
	}

}
