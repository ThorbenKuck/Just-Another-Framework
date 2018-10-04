package com.github.thorbenkuck.scripting.io;

import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.packages.Package;

public class IOModule {

	public static void apply(Parser parser) {
		parser.add(getPackage());
	}

	public static Package getPackage() {
		return Package.build()
				.add(new PrintFunction())
				.add(new PrintLineFunction())
				.create();
	}

}
