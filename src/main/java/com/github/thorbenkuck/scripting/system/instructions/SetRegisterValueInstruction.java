package com.github.thorbenkuck.scripting.system.instructions;

import com.github.thorbenkuck.scripting.Register;
import com.github.thorbenkuck.scripting.vm.Instruction;
import com.github.thorbenkuck.scripting.vm.Pointer;

public class SetRegisterValueInstruction implements Instruction {

	@Override
	public void execute(byte[] bytes, Register register, Pointer pointer) {
		byte index = bytes[0];
		byte value = bytes[1];
		register.put(index, value);
	}

	@Override
	public int requiredNoneInstructionBytes() {
		return 2;
	}

	@Override
	public byte identifier() {
		return 1;
	}
}
