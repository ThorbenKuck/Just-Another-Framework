package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.components.Rule;

import java.util.ArrayList;
import java.util.List;

class PackageFactory implements PackageBuilder {

	private final List<Function> functions = new ArrayList<>();
	private final List<Rule> rules = new ArrayList<>();

	@Override
	public PackageBuilder add(final Function toAdd) {
		functions.add(toAdd);
		return this;
	}

	@Override
	public PackageBuilder add(final Rule toAdd) {
		rules.add(toAdd);
		return this;
	}

	@Override
	public PackageBuilder add(final Package toAdd) {
		functions.addAll(toAdd.getFunctions());
		rules.addAll(toAdd.getRules());
		return this;
	}

	@Override
	public ModifiablePackage buildInMemory() {
		return new InMemoryPackage(functions, rules);
	}
}
