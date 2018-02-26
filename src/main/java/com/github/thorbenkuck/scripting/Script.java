package com.github.thorbenkuck.scripting;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class Script {

	private final Queue<Consumer<Register>> toRun = new LinkedList<>();
	private final Register register = new Register();

	Script(Queue<Consumer<Register>> core) {
		this.toRun.addAll(core);
	}

	public void run() {
		Queue<Consumer<Register>> copy = new LinkedList<>(toRun);

		while(copy.peek() != null) {
			copy.poll().accept(register);
		}

		register.clear();
	}
}
