package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.util.*;
import java.util.function.Consumer;

class ScriptImpl implements Script {

	private final Queue<Consumer<Register>> toRun = new LinkedList<>();
	private final Map<String, String> initialRegisterValues = new HashMap<>();

	ScriptImpl() {
		this(new LinkedList<>());
	}

	ScriptImpl(Queue<Consumer<Register>> core) {
		this.toRun.addAll(core);
	}

	void setConsumer(Collection<Consumer<Register>> consumer) {
		this.toRun.addAll(consumer);
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
		final Register register = new Register();
		synchronized (initialRegisterValues) {
			register.adapt(initialRegisterValues);
		}
		Queue<Consumer<Register>> copy = new LinkedList<>(toRun);

		try {
			while(copy.peek() != null) {
				Consumer<Register> consumer = copy.poll();
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
		for(Consumer<Register> consumer : toRun) {
			result.append("(").append(lineCounter++).append("): ").append(consumer).append(System.lineSeparator());
		}
		result.append("------ SCRIPT_END");
		return result.toString();
	}

	@Override
	public void setValue(String key, String value) {
		initialRegisterValues.put(key, value);
	}
}
