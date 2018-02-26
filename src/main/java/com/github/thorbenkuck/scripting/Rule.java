package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

public interface Rule {

	static Rule variableInitializer() {
		return new VariableInitializerRule();
	}

	static Rule variableDefinition() {
		return new VariableDefinitionRule();
	}

	static Rule loopStart() {
		return new LoopStartRule();
	}

	static Rule loopEnd() {
		return new LoopEndRule();
	}

	static Rule incrementInteger() {
		return new IntegerIncrementRule();
	}

	static void applyDefaults(Parser parser) {
		parser.addRule(Rule.variableInitializer());
		parser.addRule(Rule.variableDefinition());
		parser.addRule(Rule.loopStart());
		parser.addRule(Rule.loopEnd());
		parser.addRule(Rule.incrementInteger());
	}

	boolean applies(Line line);

	Consumer<Register> apply(Line line, Parser parser, int linePointer);

}
