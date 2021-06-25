package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.components.Rule;

import java.util.Collection;

public interface Package {

	static PackageBuilder build() {
		return new PackageFactory();
	}

	Collection<Function> getFunctions();

	Collection<Rule> getRules();
}
