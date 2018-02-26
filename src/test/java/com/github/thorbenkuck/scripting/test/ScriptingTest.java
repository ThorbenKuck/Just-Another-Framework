package com.github.thorbenkuck.scripting.test;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;
import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;

import java.util.*;

public class ScriptingTest {

	private static String toEvaluate = "println(get(x))";

	private static String toEvaluateHarder =
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

	public static void main(String[] args) {
		Parser parser = Parser.create();

		Rule.applyDefaults(parser);
		Function.applyDefaults(parser);

		parser.addFunction(new ExampleFunction());

		System.out.println("parsing executable script ..");
		Script script;
		try {
			script = parser.parse(toEvaluateHarder);
		} catch (ParsingFailedException e) {
			e.printStackTrace();
			return;
		}
		System.out.println("script parsed!");

		System.out.println();
		System.out.println(script);
		System.out.println();

		Scanner scanner = new Scanner(System.in);
		String entered;
		try {
			do {
				System.out.println();
				System.out.println();
				System.out.println();
				System.out.print("Set initial value of x to: ");
				entered = scanner.nextLine();
				if (!entered.equals("stop")) {

					System.out.println();
					script.setValue("x", entered);
					try {
						script.run();
					} catch (ExecutionFailedException e) {
						e.printStackTrace(System.out);
					}
				}
			} while (!entered.equals("stop"));
		} catch (IllegalStateException | NoSuchElementException e) {
			// System.in has been closed
			System.out.println("System.in was closed; exiting");
		}
	}

	private static class ExampleFunction implements Function {
		@Override
		public String getFunctionName() {
			return "get";
		}

		@Override
		public String calculate(String[] args, Register register) {
			if (args.length > 0) {
				Queue<String> leftOver = new LinkedList<>(Arrays.asList(args));
				StringBuilder result = new StringBuilder();
				while (leftOver.peek() != null) {
					String arg = leftOver.poll();
					result.append(register.get(arg));
					if (leftOver.peek() != null) {
						result.append(" AND ");
					}
				}
				return result.toString();
			}
			return asStringValue.apply("blablabla");
		}
	}
}
