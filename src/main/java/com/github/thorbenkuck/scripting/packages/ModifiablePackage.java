package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.components.Rule;

public interface ModifiablePackage extends Package {

    void addFunction(Function function);

    void addRule(Rule rule);

}
