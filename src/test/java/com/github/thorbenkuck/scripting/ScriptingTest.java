package com.github.thorbenkuck.scripting;

import java.util.function.Consumer;

public class ScriptingTest {

	private static String toEvaluate =
					"let x = 5;" +
					"loop i 1:5;" +
					"    print(i);" +
					"    print(\"=\");" +
					"    println(x);" +
					"endLoop i;" +
					"x = 6;" +
					"print(\"x should now be 6: \");" +
					"println(x);" +
					"print(\"i=\");println(i);";

	public static void main(String[] args) {
		Parser parser = new Parser();
		parser.addRule(new VariableInitializationRule());
		parser.addRule(new VariableDefinitionRule());
		parser.addRule(new PrintFunction());
		parser.addRule(new PrintLineFunction());
		parser.addRule(new LoopStartFunction());
		parser.addRule(new LoopEndFunction());

		Script script = parser.evaluate(toEvaluate);
		script.run();
		System.out.println();
		System.out.println();



		System.out.println("Done");
	}

	private static class PrintFunction implements Rule {

		@Override
		public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
			line.trimLeadingWhiteSpaces();
			String rawArgs = line.toString();
			String name = rawArgs.substring(6, rawArgs.indexOf(")"));
			if (name.startsWith("\"")) {
				String withoutLeading = name.substring(1, name.length());
				String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
				return printText(toPrint);
			} else if (!name.equals("")) {
				return printVariable(name);
			} else {
				return printText("");
			}
		}

		@Override
		public boolean applies(Line line) {
			return line.matches("print\\([a-zA-Z0-9]+\\)") || line.matches("print\\([\\\"]+(?s).*[\\\"]+\\)");
		}
	}

	private static class PrintLineFunction implements Rule {

		@Override
		public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
			line.trimLeadingWhiteSpaces();
			String rawArgs = line.toString();
			String name = rawArgs.substring(8, rawArgs.indexOf(")"));
			if (name.startsWith("\"")) {
				String withoutLeading = name.substring(1, name.length());
				String toPrint = withoutLeading.substring(0, withoutLeading.indexOf("\""));
				return printLineText(toPrint);
			} else if (!name.equals("")) {
				return printLineVariable(name);
			} else {
				return printLineText("");
			}
		}

		@Override
		public boolean applies(Line line) {
			return line.matches("println\\([\"]*[a-zA-Z0-9]+[\"]*\\)");
		}
	}

	private static class LoopStartFunction implements Rule {

		@Override
		public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
			String rawArgs = line.toString();
			String withoutIdentifier = rawArgs.substring(5, rawArgs.length());
			StringBuilder stringBuilder = new StringBuilder(withoutIdentifier);
			String name = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
			stringBuilder.delete(0, name.length() + 1);
			String initial = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
			stringBuilder.delete(0, name.length() + 1);
			String finale = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
			stringBuilder.delete(0, name.length() + 1);
			if (parser.getInternalVariable(name) != null) {
				int value = Integer.parseInt(parser.getInternalVariable(name));
				++value;
				String stringedValue = String.valueOf(value);
				int finalValue = Integer.parseInt(finale);
				if (value > finalValue) {
					parser.setLinePointer(Integer.parseInt(parser.getInternalVariable("loopEnd" + name)));
					parser.deleteInternalVariable(name);
					parser.clearInternalVariable("loop" + name);
					parser.clearInternalVariable("loopEnd" + name);
					return register -> register.remove(name);
				} else {
					parser.setInternalVariable(name, stringedValue);
					return register -> {
						int count = Integer.parseInt(register.get(name));
						++count;
						register.put(name, String.valueOf(count));
					};
				}
			} else {
				parser.setInternalVariable(name, initial);
				parser.setInternalVariable("loop" + name, String.valueOf(linePointer - 1));
				return register -> register.put(name, initial);
			}
		}

		@Override
		public boolean applies(Line line) {
			// return line.startsWith("loop [a-bA-B0-9]+ [0-9]+ : [0-9]+");
			return line.startsWith("loop");
		}
	}

	private static class LoopEndFunction implements Rule {

		@Override
		public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
			String rawArgs = line.toString();
			String variableName = rawArgs.substring(8, rawArgs.length());

			if (parser.getInternalVariable("loopEnd" + variableName) == null) {
				parser.setInternalVariable("loopEnd" + variableName, String.valueOf(linePointer));
			}
			if (parser.getInternalVariable(variableName) != null) {
				parser.setLinePointer(Integer.parseInt(parser.getInternalVariable("loop" + variableName)));
			}
			return null;
		}

		@Override
		public boolean applies(Line line) {
			return line.matches("endLoop [a-zA-Z0-9]+");
		}
	}

	private static class VariableInitializationRule implements Rule {

		@Override
		public boolean applies(Line line) {
			return line.matches("let [a-zA-Z0-9]+.*");
		}

		@Override
		public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
			StringBuilder stringBuilder = new StringBuilder(line.toString());
			String name = parseVariableName(stringBuilder);
			line.remove(0, 3);
			return new Consumer<Register>() {
				@Override
				public void accept(Register register) {
					if(register.get(name) == null) {
						register.put(name, "undefined");
					} else {
						parser.error(name + " is already defined!");
					}
				}

				@Override
				public String toString() {
					return "InitializeVariable(" + name + ")";
				}
			};
		}

		private String parseVariableName(StringBuilder line) {
			if (!(line.indexOf("let ") == 0)) {
				return "null";
			}

			line.delete(0, "let ".length());

			String lineString = line.toString();

			String name = line.substring(0, lineString.indexOf(" "));
			line.delete(0, name.length());

			return name;
		}
	}

	private static class VariableDefinitionRule implements Rule {

		@Override
		public boolean applies(Line line) {
			return line.matches("[a-zA-Z1-9]+ = [a-zA-Z1-9]+");
		}

		@Override
		public Consumer<Register> apply(Line line, Parser parser, int linePointer) {
			line.trimWhiteSpaces();
			StringBuilder stringBuilder = new StringBuilder(line.toString());
			String name = parseVariableName(stringBuilder);
			return new Consumer<Register>() {
				@Override
				public void accept(Register register) {
					if(register.get(name) == null) {
						parser.error(name + " is not defined");
					} else {
						register.put(name, stringBuilder.toString());
					}
				}

				@Override
				public String toString() {
					return "SetVariable(" + name + " to " + stringBuilder.toString() + ")";
				}
			};
		}

		private String parseVariableValue(StringBuilder stringBuilder) {
			if (stringBuilder.toString().startsWith("=")) {
				stringBuilder.delete(0, 1);
			}

			while (stringBuilder.toString().startsWith(" ") || stringBuilder.toString().startsWith("=")) {
				stringBuilder.delete(0, 1);
			}

			return stringBuilder.toString();
		}

		private String parseVariableName(StringBuilder line) {
			String lineString = line.toString();

			String name = line.substring(0, lineString.indexOf("="));
			line.delete(0, name.length() + 1);

			return name;
		}
	}

	private static Consumer<Register> printVariable(String variableName) {
		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.print(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintVariable(" + variableName + ")";
			}
		};
	}

	private static Consumer<Register> printText(String what) {

		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.print(what);
			}

			@Override
			public String toString() {
				return "PrintText(\"" + what + "\")";
			}
		};
	}

	private static Consumer<Register> printLineVariable(String variableName) {
		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.println(register.get(variableName));
			}

			@Override
			public String toString() {
				return "PrintlnVariable(" + variableName + ")";
			}
		};
	}

	private static Consumer<Register> printLineText(String what) {
		return new Consumer<Register>() {
			public void accept(Register register) {
				System.out.println(what);
			}

			@Override
			public String toString() {
				return "PrintlnText(\"" + what + "\")";
			}
		};
	}
}
