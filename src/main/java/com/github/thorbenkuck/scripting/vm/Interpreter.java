package com.github.thorbenkuck.scripting.vm;

import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.exceptions.IllegalByteCodeException;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

	private final Register register;
	private final InstructionSet instructionSet;
	private final Pointer pointer;
	private final List<Byte> nonInstructionBuffer = new ArrayList<>();
	private Instruction instruction;
	private byte currentInstructionByte;

	public Interpreter(Register register, Pointer pointer, InstructionSet instructionSet) {
		this.register = register;
		this.pointer = pointer;
		this.instructionSet = instructionSet;
	}

	public boolean prepare(byte b) {
		if (instruction == null) {
			currentInstructionByte = b;
			instruction = instructionSet.lookup(b);
		} else {
			nonInstructionBuffer.add(b);
		}

		return nonInstructionBuffer.size() == instruction.requiredNoneInstructionBytes();
	}

	public void execute() {
		byte[] nonInstructionBytes = new byte[nonInstructionBuffer.size()];
		for (int i = 0; i < nonInstructionBuffer.size(); i++) {
			nonInstructionBytes[i] = nonInstructionBuffer.get(i);
		}

		nonInstructionBuffer.clear();

		if (instruction == null) {
			throw new IllegalByteCodeException("No bytes prepared!");
		}

		try {
			instruction.execute(nonInstructionBytes, register, pointer);
		} catch (IllegalByteCodeException e) {
			e.addSuppressed(new IllegalByteCodeException("Error while executing " + currentInstructionByte));
			throw e;
		}

		instruction = null;
		currentInstructionByte = 0;
	}
}
