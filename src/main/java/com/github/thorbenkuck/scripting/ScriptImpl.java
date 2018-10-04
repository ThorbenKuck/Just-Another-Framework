package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.time.LocalDateTime;
import java.util.*;

class ScriptImpl implements Script {

	private final Queue<ScriptElement<Register>> core = new LinkedList<>();
	private final Map<String, String> initialRegisterValues = new HashMap<>();
	private final LocalDateTime timeOfCreation = LocalDateTime.now();
	private String name = "Script(" + timeOfCreation + ")";

	ScriptImpl(Queue<ScriptElement<Register>> core) {
		this.core.addAll(core);
	}

	@Override
	public void addInstruction(ScriptElement<Register> instruction) {
		synchronized (core) {
			core.add(instruction);
		}
	}

	@Override
	public void run(Map<String, String> registerValues) throws ExecutionFailedException {
		synchronized (initialRegisterValues) {
			initialRegisterValues.putAll(registerValues);
		}
		run();
	}

	@Override
	public void run() throws ExecutionFailedException {
		final Register register = Register.create();
		synchronized (initialRegisterValues) {
			register.adapt(initialRegisterValues);
		}
		Queue<ScriptElement<Register>> copy;
		synchronized (core) {
			copy = new LinkedList<>(core);
		}

		try {
			while(copy.peek() != null) {
				ScriptElement<Register> consumer = copy.poll();
				consumer.accept(register);
			}
		} catch (Exception e) {
			throw new ExecutionFailedException(e);
		} finally {
			register.clear();
		}
	}

	@Override
	public String toString() {
		int lineCounter = 0;
		StringBuilder result = new StringBuilder(getName());
		result.append("------ SCRIPT_START").append(System.lineSeparator());

		final List<ScriptElement<Register>> consumerCopy;
		synchronized (core) {
			consumerCopy = new ArrayList<>(core);
		}

		for(ScriptElement<Register> consumer : consumerCopy) {
			result.append("(").append(lineCounter++).append("): ").append(consumer).append(System.lineSeparator());
		}
		result.append("------ SCRIPT_END");
		return result.toString();
	}

	@Override
	public void setValue(String key, String value) {
		initialRegisterValues.put(key, value);
	}

	@Override
	public int countInstructions() {
		synchronized (core) {
			return core.size();
		}
	}

	@Override
	public void setName(String string) {
		this.name = string;
	}

	@Override
	public LocalDateTime getTimeOfCreation() {
		return timeOfCreation;
	}

	@Override
	public String getName() {
		return name;
	}
}
