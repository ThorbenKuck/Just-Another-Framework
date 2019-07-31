package com.github.thorbenkuck.scripting.vm;

import com.github.thorbenkuck.scripting.Register;

public interface Instruction {

	void execute(byte[] bytes, Register register, Pointer pointer);

	int requiredNoneInstructionBytes();

	byte identifier();

}
