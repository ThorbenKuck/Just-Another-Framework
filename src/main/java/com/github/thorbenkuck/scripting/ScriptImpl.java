package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.util.*;
import java.util.function.Consumer;

class ScriptImpl implements Script {

	private final Queue<Consumer<Register>> core = new LinkedList<>();
	private final Map<String, String> initialRegisterValues = new HashMap<>();

	ScriptImpl(Queue<Consumer<Register>> core) {
		this.core.addAll(core);
	}

	void setInstructions(Collection<Consumer<Register>> consumer) {
		synchronized (core) {
			core.clear();
			core.addAll(consumer);
		}
	}

	@Override
	public void addInstruction(Consumer<Register> instruction) {
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
		Queue<Consumer<Register>> consumerCopy;
		synchronized (core) {
			consumerCopy = new LinkedList<>(core);
		}

		try {
			while(consumerCopy.peek() != null) {
				Consumer<Register> consumer = consumerCopy.poll();
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
		StringBuilder result = new StringBuilder("------ SCRIPT_START" + System.lineSeparator());

		final List<Consumer<Register>> consumerCopy;
		synchronized (core) {
			consumerCopy = new ArrayList<>(core);
		}

		for(Consumer<Register> consumer : consumerCopy) {
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
}
