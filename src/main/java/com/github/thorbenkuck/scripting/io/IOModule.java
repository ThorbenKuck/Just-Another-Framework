package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.packages.Package;
import com.github.thorbenkuck.scripting.packages.PackageBuilder;

public class IOModule {

	public static void applyAll(Parser parser) {
		parser.add(getPackage());
	}

	public static Package getPackage() {
		return PackageBuilder.get()
				.add(new PrintFunction())
				.add(new PrintLineFunction())
				.create();
	}

}
