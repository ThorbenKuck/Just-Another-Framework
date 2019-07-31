package com.github.thorbenkuck.scripting.vm;

import com.github.thorbenkuck.scripting.exceptions.IllegalPointerStateException;

public class Pointer {

	private int pointer;

	private Pointer(int pointer) {
		this.pointer = pointer;
	}

	public static Pointer create() {
		return at(0);
	}

	public static Pointer at(int i) {
		return new Pointer(i);
	}

	private void checkRange(Pointer pointer) {
		if (pointer.get() < 0) {
			throw new IllegalPointerStateException("pointer cannot be set to less than 0");
		}
	}

	public void increase() {
		pointer += 1;
	}

	public void decrease() {
		pointer += 1;
	}

	public void set(int i) {
		pointer = i;
		checkRange(this);
	}

	public int get() {
		return pointer;
	}
}
