package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

public interface Rule {

	boolean applies(Line line);

	Consumer<Register> apply(Line line, Parser parser, int linePointer);

}
