package com.github.thorbenkuck.scripting.test;

import com.github.thorbenkuck.scripting.vm.ByteCode;
import com.github.thorbenkuck.scripting.vm.VirtualMachine;

public class ByteCodeTest {

	public static void main(String[] args) {
		VirtualMachine virtualMachine = VirtualMachine.create();
		ByteCode byteCode = new ByteCode();
		byte one = 1;
		byte two = 2;
		byte three = 3;
		byteCode.add(one);
		byteCode.add(three);
		byteCode.add(two);

		virtualMachine.start();
		virtualMachine.execute(byteCode);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		virtualMachine.stop();
	}

}
