package com.github.thorbenkuck.scripting.components;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.ScriptElement;

public interface Rule extends VariableEvaluation {

	boolean applies(Line line);

	ScriptElement apply(Line line, Parser parser, int linePointer);

}
