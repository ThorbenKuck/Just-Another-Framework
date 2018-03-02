package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class PackageImpl implements Package {

	private final List<Function> functionList = new ArrayList<>();
	private final List<Rule> ruleList = new ArrayList<>();

	public PackageImpl(Collection<Function> functionCollection, Collection<Rule> ruleCollection) {
		functionList.addAll(functionCollection);
		ruleList.addAll(ruleCollection);
	}

	@Override
	public Collection<Function> getFunctions() {
		return new ArrayList<>(functionList);
	}

	@Override
	public Collection<Rule> getRules() {
		return new ArrayList<>(ruleList);
	}

	@Override
	public void addFunction(Function function) {
		functionList.add(function);
	}

	@Override
	public void addRule(Rule rule) {
		ruleList.add(rule);
	}

}
