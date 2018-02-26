A "simple" foundation to creating your own scripting Language.

------

#!WARNING!

This Framwork ist still in production! Use at your own Risk!

------

A simple Example:

´´´java
// The Script we want to evaluate.
// Since this is only one line, we have to put ; as a crft
// The intends are optional
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
			"var z;" +
			"print(\"z=\");println(z);" +
			"println(\"END\");";

// Creates a new Parser.
// Every Parser is unique and
// maintains its own set of rules
// and functions.
Parser parser = Parser.create();

// Hook up all your rules and functions
// Here, we use the provided ones and
// simply apply all default Rules
// and Functions
Rule.applyDefaults(parser);
Function.applyDefaults(parser);

// Parse the Script.
// This will return an executable script
Script script;
try {
	script = parser.parse(toEvaluateHarder);
} catch (ParsingFailedException e) {
	e.printStackTrace();
	return;
}

System.out.println("script parsed!");

System.out.println();
// Prints every step the script is going to take
System.out.println(script);
System.out.println();

// Inject "outside" variables.
// You can also set them, once you
// call Script#run by providing
// a Map<String, String> which
// maps variable names to its values.
// 
// In Script-World, we are only know
// String. We may however convert those
// to Objects as we execute our java-code
script.setValue("x", "10");

// Now we run our Script. This may throw an
// ExecutionFailedException if anything
// goes wrong as we run it.
try {
  script.run();
} catch (ExecutionFailedException e) {
	e.printStackTrace(System.out);
}
```
