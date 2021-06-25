package com.github.thorbenkuck.scripting.script;

import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;
import com.github.thorbenkuck.scripting.script.ScriptElement;

import java.time.LocalDateTime;
import java.util.*;

public class Script {

	private final Queue<ScriptElement> core = new LinkedList<>();
	private final Map<String, String> initialRegisterValues = new HashMap<>();
	private final LocalDateTime timeOfCreation = LocalDateTime.now();
	private String name = "Script(" + timeOfCreation + ")";

	public Script(Queue<ScriptElement> core) {
		this.core.addAll(core);
	}

	public void addInstruction(ScriptElement instruction) {
		synchronized (core) {
			core.add(instruction);
		}
	}

	private void doRun(Register register) throws ExecutionFailedException {
		synchronized (initialRegisterValues) {
			register.adapt(initialRegisterValues);
		}
		Queue<ScriptElement> copy;
		synchronized (core) {
			copy = new LinkedList<>(core);
		}

		try {
			while(copy.peek() != null) {
				ScriptElement consumer = copy.poll();
				consumer.accept(register);
			}
		} catch (Exception e) {
			throw new ExecutionFailedException(e);
		} finally {
			register.clear();
		}
	}

	public void run(Map<String, String> registerValues) throws ExecutionFailedException {
		doRun(new Register(registerValues));
	}

	public void run() throws ExecutionFailedException {
		doRun(new Register());
	}

	public void setValue(String key, String value) {
		initialRegisterValues.put(key, value);
	}

	public int countInstructions() {
		synchronized (core) {
			return core.size();
		}
	}

	public void setName(String string) {
		this.name = string;
	}

	public LocalDateTime getTimeOfCreation() {
		return timeOfCreation;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		int lineCounter = 0;
		StringBuilder result = new StringBuilder(getName());
		result.append("------ SCRIPT_START").append(System.lineSeparator());

		final List<ScriptElement> consumerCopy;
		synchronized (core) {
			consumerCopy = new ArrayList<>(core);
		}

		for(ScriptElement consumer : consumerCopy) {
			result.append("(").append(lineCounter++).append("): ").append(consumer.describe()).append(System.lineSeparator());
		}
		result.append("------ SCRIPT_END");
		return result.toString();
	}
}
