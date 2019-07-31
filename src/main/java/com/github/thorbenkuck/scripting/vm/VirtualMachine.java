package com.github.thorbenkuck.scripting.vm;

public interface VirtualMachine extends Runnable {

	static VirtualMachine create(InstructionSet instructionSet) {
		return new NativeVirtualMachine(instructionSet);
	}

	static VirtualMachine create() {
		return create(InstructionSet.standard());
	}

	void start();

	void stop();

	void execute(ByteCode byteCode);
}
