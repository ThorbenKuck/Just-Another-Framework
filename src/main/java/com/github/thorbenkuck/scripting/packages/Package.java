package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Rule;

import java.util.Collection;

public interface Package {
	Collection<Function> getFunctions();

	Collection<Rule> getRules();

	void addFunction(Function function);

	void addRule(Rule rule);
}
