package com.github.thorbenkuck.scripting.test;

import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.Script;
import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;
import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;
import com.github.thorbenkuck.scripting.io.PrintLineFunction;
import com.github.thorbenkuck.scripting.math.IncrementRule;
import com.github.thorbenkuck.scripting.packages.Package;
import com.github.thorbenkuck.scripting.system.LoopEndRule;
import com.github.thorbenkuck.scripting.system.LoopStartRule;
import com.github.thorbenkuck.scripting.system.VariableDefinitionRule;
import com.github.thorbenkuck.scripting.system.VariableInitializerRule;

public class BenchmarkTest {

	public static void main(String[] args) {
		String rawScript = "var x = 4;" +
				"loop i 1:10000;" +
				"    ++x;" +
				"endLoop i;" +
				"println(\"x=\", x)";

		Package foundation = Package.build()
				.add(new LoopStartRule())
				.add(new LoopEndRule())
				.add(new IncrementRule())
				.add(new VariableInitializerRule())
				.add(new VariableDefinitionRule())
				.add(new PrintLineFunction(null))
				.buildInMemory();

		int counter = 0;

		System.out.println("| Script-Creation | Script-Execution | Java-Execution |\n" +
				"| :-------------: | :--------------: | :------------: |");

		while (counter < 50) {

			Script script;
			Parser parser = new Parser();

			parser.add(foundation);

			long startCreation = System.currentTimeMillis();
			try {
				script = parser.parse(rawScript);
			} catch (ParsingFailedException e) {
				e.printStackTrace();
				return;
			}
			long timeTakenCreation = System.currentTimeMillis() - startCreation;

			long startScriptExecution = System.currentTimeMillis();

			try {
				script.run();
			} catch (ExecutionFailedException e) {
				e.printStackTrace();
			}
			long timeTakenScriptExecution = System.currentTimeMillis() - startScriptExecution;

			long startJavaExecution = System.currentTimeMillis();

			int x = 4;

			for (int i = 1; i <= 10000; i++) {
				++x;
			}

//			System.out.println("x = " + x);

			long timeTakenJavaExecution = System.currentTimeMillis() - startJavaExecution;

			System.out.println("| " + timeTakenCreation + " millis | " + timeTakenScriptExecution + " millis | " + timeTakenJavaExecution + " millis |");

			++counter;
		}
	}
}
