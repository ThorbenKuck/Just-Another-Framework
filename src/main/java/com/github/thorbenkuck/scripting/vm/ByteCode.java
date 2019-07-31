package com.github.thorbenkuck.scripting.vm;

import java.util.LinkedList;
import java.util.List;

public class ByteCode {

	private final List<Byte> byteList = new LinkedList<>();

	public void add(byte b) {
		byteList.add(b);
	}

	public void add(byte[] bytes) {
		add(bytes, 0, bytes.length);
	}

	public void add(byte[] bytes, int offset, int length) {
		for (int i = offset; i < (offset + length); i++) {
			add(bytes[i]);
		}
	}

	public void add(ByteCode byteCode) {
		add(byteCode.toByteCode());
	}

	public byte[] toByteCode() {
		byte[] bytes = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); i++) {
			bytes[i] = byteList.get(i);
		}

		return bytes;
	}

	public void clear() {
		byteList.clear();
	}
}
