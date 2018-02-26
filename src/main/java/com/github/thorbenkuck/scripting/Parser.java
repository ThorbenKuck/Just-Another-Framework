package com.github.thorbenkuck.scripting;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Parser {

	private final List<Rule> rules = new ArrayList<>();
	private final Map<String, String> internalVariables = new HashMap<>();
	// TODO: vlt mit strategyPattern dynamisch austauschbar machen
	private final String lineEnd = ";";
	private final AtomicInteger linePointer = new AtomicInteger();
	private final AtomicBoolean running = new AtomicBoolean(false);

	public synchronized Script evaluate(String string) {
		running.set(true);
		final Queue<Consumer<Register>> created = new LinkedList<>();
		linePointer.set(0);
		List<Line> lines = parseLines(string);
		for(int currentLine = 0 ; currentLine < lines.size() ;) {
			if(!running.get()) {
				return new Script(created);
			}
			Line line = lines.get(currentLine);
			for(Rule rule : rules) {
				if(rule.applies(line)) {
					Consumer<Register> consumer = rule.apply(line, this, linePointer.get());
					if(consumer != null) {
						created.add(consumer);
					}
				}
			}
			currentLine = linePointer.incrementAndGet();
		}

		internalVariables.clear();
		running.set(false);

		return new Script(created);
	}

	private List<Line> parseLines(String wholeText) {
		List<Line> lines = new ArrayList<>();
		StringBuilder complete = new StringBuilder(wholeText);
		while(!complete.toString().isEmpty()) {
			String line = complete.substring(0, complete.indexOf(lineEnd));
			while(line.startsWith(" ")) {
				line = line.substring(1, line.length());
			}

			if(!line.isEmpty()) {
				lines.add(new Line(line));
			}
			complete.delete(0, line.length() + 1);
		}

		return lines;
	}

	public void setLinePointer(int to) {
		this.linePointer.set(to);
	}

	public void setInternalVariable(String key, String value) {
		this.internalVariables.put(key, value);
	}

	public String getInternalVariable(String key) {
		return this.internalVariables.get(key);
	}

	public void addRule(Rule rule) {
		this.rules.add(rule);
	}

	public void error(String s) {
		running.set(false);
		System.out.println("[ERROR]: " + s);
	}

	public void clearInternalVariable(String name) {
		internalVariables.remove(name);
	}

	@Override
	public String toString() {
		return "Parser{" +
				"rules=" + rules +
				", internalVariables=" + internalVariables +
				", lineEnd='" + lineEnd + '\'' +
				", linePointer=" + linePointer +
				", running=" + running +
				'}';
	}

	public void deleteInternalVariable(String name) {
		internalVariables.remove(name);
	}
}
