package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.Parser;
import com.github.thorbenkuck.scripting.packages.Package;

public class SystemModule {

	public static void applyAll(Parser parser) {
		parser.add(getPackage());
	}

	public static Package getPackage() {
		return Package.build()
				.add(new ConvertToIntegerFunction())
				.add(new RequireIsSetFunction())
				.add(new VariableInitializerRule())
				.add(new VariableDefinitionRule())
				.add(new DeleteRule())
				.add(new LoopStartRule())
				.add(new LoopEndRule())
				.create();
	}
}
