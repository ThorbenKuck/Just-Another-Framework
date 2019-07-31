package com.github.thorbenkuck.scripting.vm;

import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.exceptions.IllegalByteCodeException;

import java.util.HashMap;
import java.util.Map;

public class NativeInstructionSet implements InstructionSet {

	private static final Instruction NULL_INSTRUCTION = new EmptyInstruction();
	private final Map<Byte, Instruction> instructionMap = new HashMap<>();

	@Override
	public Instruction lookup(byte b) {
		return instructionMap.getOrDefault(b, NULL_INSTRUCTION);
	}

	@Override
	public void insert(Instruction instruction) {
		instructionMap.put(instruction.identifier(), instruction);
	}

	private static final class EmptyInstruction implements Instruction {

		@Override
		public void execute(byte[] bytes, Register register, Pointer pointer) {
			throw new IllegalByteCodeException("Unknown byte identifier");
		}

		@Override
		public int requiredNoneInstructionBytes() {
			return -1;
		}

		@Override
		public byte identifier() {
			return 0;
		}
	}

}
