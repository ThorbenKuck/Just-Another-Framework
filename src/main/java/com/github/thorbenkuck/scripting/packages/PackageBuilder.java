package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.Function;
import com.github.thorbenkuck.scripting.Rule;

public interface PackageBuilder {

	static PackageBuilder get() {
		return new PackageFactory();
	}

	PackageBuilder add(Function toAdd);

	PackageBuilder add(Rule toAdd);

	PackageBuilder add(Package toAdd);

	Package create();

}
