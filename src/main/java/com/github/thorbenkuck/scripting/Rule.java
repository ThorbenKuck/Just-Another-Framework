package com.github.thorbenkuck.scripting;

public interface Rule extends VariableEvaluation {

	boolean applies(Line line);

	ScriptElement<Register> apply(Line line, Parser parser, int linePointer);

}
