package com.github.thorbenkuck.scripting.vm;

import com.github.thorbenkuck.scripting.Register;

public interface InstructionSet {

	static InstructionSet empty() {
		return new NativeInstructionSet();
	}

	static InstructionSet standard() {
		InstructionSet instructionSet = empty();
		instructionSet.insert(new Instruction() {
			@Override
			public void execute(byte[] bytes, Register register, Pointer pointer) {
				register.put("1", String.valueOf(bytes[0]));
			}

			@Override
			public int requiredNoneInstructionBytes() {
				return 1;
			}

			@Override
			public byte identifier() {
				return 1;
			}
		});
		instructionSet.insert(new Instruction() {
			@Override
			public void execute(byte[] bytes, Register register, Pointer pointer) {
				String value = register.get("1");
				System.out.println(value);
			}

			@Override
			public int requiredNoneInstructionBytes() {
				return 0;
			}

			@Override
			public byte identifier() {
				return 2;
			}
		});
		return instructionSet;
	}

	Instruction lookup(byte b);

	void insert(Instruction instruction);
}
