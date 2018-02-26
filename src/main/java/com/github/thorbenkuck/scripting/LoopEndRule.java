package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

class LoopEndRule implements Rule {


	@Override
	public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
		String rawArgs = line.toString();
		String variableName = rawArgs.substring(8, rawArgs.length());

		if (Register.NULL_VALUE.equals(parser.getInternalVariable("loopEnd" + variableName))) {
			parser.setInternalVariable("loopEnd" + variableName, String.valueOf(linePointer));
		}
		if (! Register.NULL_VALUE.equals(parser.getInternalVariable(variableName))) {
			parser.setLinePointer(Integer.parseInt(parser.getInternalVariable("loop" + variableName)));
		}
		return null;
	}

	@Override
	public boolean applies(Line line) {
		return line.matches("endLoop [a-zA-Z0-9]+");
	}

}
