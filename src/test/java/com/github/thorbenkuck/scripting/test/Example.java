package com.github.thorbenkuck.scripting.test;

import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.Script;
import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;
import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.io.IOModule;
import com.github.thorbenkuck.scripting.math.MathModule;
import com.github.thorbenkuck.scripting.packages.Package;
import com.github.thorbenkuck.scripting.system.SystemModule;

import javax.script.ScriptEngineManager;

// This is the Example from the Readme, made executable
public class Example {

	public static void main(String[] args) {
		new Example().run();
	}

	private void run() {
		// The Script we want to evaluate.
		// Since this is only one line, we have to put ; as a crlf
		// The intends are optional.
		// Every function and rule is supported by this framework
		// This is the best i could come up with.. sorry for
		// possible confusions.. Let me explain those functions:

		//print(..)       prints something
		//println(..)     prints something followed by a new line
		//require($)      throws an exception, if variable($) is not set
		//convertToInt($) sets variable($) to 0, if it is not an int in java
		//loop $ x:y      loops from x to y, increasing variable($)
		//++$             increments a variable($)
		//var $           marks an variable as usable($)
		//$ = %           sets the usable variable $ to %
		String toEvaluate =
				"println(\"DEMO-SCRIPT!\");" +
						"require(x);" +
						"convertToInt(x);" +
						"var y = x;" +
						"require(y);" +
						"loop i 1:5;" +
						"    print(i);" +
						"    print(\"=\");" +
						"    println(y);" +
						"    loop j 1:3;" +
						"        ++y;" +
						"    endLoop j;" +
						"endLoop i;" +
						"print(\"x should still be what you selected: \");" +
						"println(x);" +
						"print(\"i=\");" +
						"println(i);" +
						"print(\"j=\");" +
						"println(j);" +
						"var z;" +
						"print(\"z=\");println(z);" +
						"println(\"END\");";

		// Creates a new Parser.
		// Every Parser is unique and
		// maintains its own set of rules
		// and functions.
		Parser parser = new Parser();

		// Hook up all your rules and functions
		// Here, we use the provided ones and
		// simply apply all default Rules
		// and Functions.
		// Make sure, that you import the
		// correct Package. Java has its own
		// package, which is part of java.lang.
		// This means, if you import nothing,
		// you will get an error, because the
		// PackageBuilder returns the wrong type
		// of package.
		Package foundation = Package.build()
				.add(IOModule.getPackage())
				.add(SystemModule.getPackage())
				.add(MathModule.getPackage())
				.buildInMemory();

		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

		parser.add(foundation);

		// Parse the Script.
		// This will return an executable script
		Script script;
		try {
			script = parser.parse(toEvaluate);
		} catch (ParsingFailedException e) {
			e.printStackTrace();
			return;
		}

		System.out.println("script parsed!");

		// Prints every step the script is going to take
		// If the Output is to much for you, simply delete
		// this following 3 lines.
		System.out.println();
		System.out.println(script);
		System.out.println();

		// Inject "outside" variables.
		// You can also set them, once you
		// call Script#run by providing
		// a Map<String, String> which
		// maps variable names to its values.
		// In Script-World, we are only know
		// Strings, which we might convert
		// to Objects as we execute our java-code
		script.setValue("x", "10");

		// Now we run our Script. This may throw an
		// ExecutionFailedException if anything
		// goes wrong as we run it. Should not
		// happen here tho
		try {
			script.run();
		} catch (ExecutionFailedException e) {
			e.printStackTrace(System.out);
		}
	}
}
