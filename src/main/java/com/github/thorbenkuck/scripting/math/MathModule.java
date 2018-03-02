package com.github.thorbenkuck.scripting.math;

import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.packages.Package;
import com.github.thorbenkuck.scripting.packages.PackageBuilder;

public class MathModule {

	public static void applyAll(Parser parser) {
		parser.add(getPackage());
	}

	public static Package getPackage() {
		return PackageBuilder.get()
				.add(new AddFunction())
				.add(new SubtractFunction())
				.add(new PowerFunction())
				.add(new DivideFunction())
				.add(new IncrementRule())
				.add(new DecrementRule())
				.add(new AddRule())
				.create();
	}

}
