package com.github.thorbenkuck.scripting.system;

import com.github.thorbenkuck.scripting.*;
import com.github.thorbenkuck.scripting.components.Rule;
import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.Parser;
import com.github.thorbenkuck.scripting.script.ScriptElement;

public class LoopStartRule implements Rule {

	@Override
	public ScriptElement apply(Line line, Parser parser, int linePointer) {
		String rawArgs = line.toString();
		String withoutIdentifier = rawArgs.substring(5, rawArgs.length());
		StringBuilder stringBuilder = new StringBuilder(withoutIdentifier);
		String name = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
		stringBuilder.delete(0, name.length() + 1);
		String initial = stringBuilder.substring(0, withoutIdentifier.indexOf(" "));
		stringBuilder.delete(0, name.length() + 1);
		String finale = stringBuilder.substring(0, stringBuilder.toString().length());
		stringBuilder.delete(0, name.length() + 1);
		Register parserRegister = parser.getParserRegister();
		if (parserRegister.has(name)) {
			int value = Integer.parseInt(parserRegister.get(name));
			++value;
			String stringedValue = String.valueOf(value);
			int finalValue = Integer.parseInt(finale);
			if (value > finalValue) {
				parser.setLinePointer(Integer.parseInt(parserRegister.get("loopEnd" + name)));
				parser.deleteInternalVariable(name);
				parserRegister.remove("loop" + name);
				parserRegister.remove("loopEnd" + name);
				return new ScriptElement() {
					@Override
					public void accept(Register register) {
						register.remove(name);
					}

					@Override
					public String toString() {
						return "Loop Finalizer. Removing " + name;
					}
				};
			} else {
				parserRegister.put(name, stringedValue);
				return new ScriptElement() {
					@Override
					public void accept(Register register) {
						int count = Integer.parseInt(register.get(name));
						++count;
						register.put(name, String.valueOf(count));

					}

					@Override
					public String toString() {
						return "Increase Loop-Count(" + name + ")";
					}
				};
			}
		} else {
			parserRegister.put(name, initial);
			parserRegister.put("loop" + name, String.valueOf(linePointer - 1));
			return new ScriptElement() {
				@Override
				public void accept(Register register) {
					register.put(name, initial);
				}

				@Override
				public String toString() {
					return "Loop initializer";
				}
			};
		}
	}

	@Override
	public boolean applies(Line line) {
		// return line.startsWith("loop [a-bA-B0-9]+ [0-9]+ : [0-9]+");
		return line.startsWith("loop");
	}

}
