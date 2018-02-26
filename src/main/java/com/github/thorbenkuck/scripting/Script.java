package com.github.thorbenkuck.scripting;

import com.github.thorbenkuck.scripting.exceptions.ExecutionFailedException;

import java.util.*;
import java.util.function.Consumer;

public class Script {

	private final Queue<Consumer<Register>> toRun = new LinkedList<>();
	private final Map<String, String> initialRegisterValues = new HashMap<>();

	Script() {
		this(new LinkedList<>());
	}

	Script(Queue<Consumer<Register>> core) {
		this.toRun.addAll(core);
	}

	void setConsumer(Collection<Consumer<Register>> consumer) {
		this.toRun.addAll(consumer);
	}

	public void run(Map<String, String> registerValues) throws ExecutionFailedException {
		synchronized (initialRegisterValues) {
			initialRegisterValues.putAll(registerValues);
		}
		run();
	}

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
		return toRun.toString();
	}

	public void setValue(String key, String value) {
		initialRegisterValues.put(key, value);
	}
}
