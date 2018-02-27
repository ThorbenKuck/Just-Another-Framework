package com.github.thorbenkuck.scripting.test;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;
import com.github.thorbenkuck.scripting.exceptions.ParsingFailedException;

import java.util.*;
import java.util.function.Consumer;

public class ScriptingTest {

	private static String toEvaluate = "println(add(add(add(add(x), x), add(x), add(x)), add(x), x));" +
			"println(get(x))";

	private static String toEvaluateHarder =
			"println(\"DEMO-SCRIPT!\");" +
					"require(x);" +
					"println(arr[ab]);" +
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
					"delete z;" +
					"print(\"z=\");println(z);" +
					"println(\"END\");";

	public static void main(String[] args) {
		Parser parser = Parser.create();

		parser.addFunction(new ExampleFunction());
		Rule.applyDefaults(parser);
		Function.applyDefaults(parser);


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
			return "add";
		}

		@Override
		public String calculate(String[] args, Register register) {
			int count = 0;

			if (args.length > 0) {
				Queue<String> leftOver = new LinkedList<>(Arrays.asList(args));
				StringBuilder result = new StringBuilder();
				while (leftOver.peek() != null) {
					String arg = leftOver.poll();
					String value;
					if(isVariable.apply(arg)) {
						value = register.get(arg);
					} else {
						value = arg;
					}
					result.append(value);
					if(Utility.isInteger(value)) {
						count += Integer.parseInt(value);
					} else {
						count += 0;
					}
					if (leftOver.peek() != null) {
						result.append(" AND ");
					}
				}
				if(args.length > 1) {
					result.append(" = ").append(count);
				}
				System.out.println(result.toString());

				return Integer.toString(count);
			}

			return String.valueOf(count);
		}
	}
}
