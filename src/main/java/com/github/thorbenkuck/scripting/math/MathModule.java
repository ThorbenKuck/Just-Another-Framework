package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.packages.Package;

public class MathModule {

	public static void applyAll(Parser parser) {
		parser.add(getPackage());
	}

	public static Package getPackage() {
		return Package.build()
				.add(new AddFunction())
				.add(new SubtractFunction())
				.add(new PowerFunction())
				.add(new DivideFunction())
				.add(new IncrementRule())
				.add(new DecrementRule())
				.add(new AddRule())
				.buildInMemory();
	}

}
