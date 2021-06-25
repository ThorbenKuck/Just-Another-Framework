package com.github.thorbenkuck.scripting.packages;

import com.github.thorbenkuck.scripting.components.Function;
import com.github.thorbenkuck.scripting.components.Rule;

public interface PackageBuilder {

	/**
	 * @return a new PackageBuilder
	 * @see Package#build()
	 * @deprecated This method is not as well worded as the current use! Use {@link Package#build()}
	 */
	@Deprecated
	static PackageBuilder get() {
		return new PackageFactory();
	}

	PackageBuilder add(Function toAdd);

	PackageBuilder add(Rule toAdd);

	PackageBuilder add(Package toAdd);

	ModifiablePackage buildInMemory();

}
