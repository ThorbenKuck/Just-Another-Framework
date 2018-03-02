package com.github.thorbenkuck.scripting;

import java.util.List;

@FunctionalInterface
public interface LineProvider {

	String LINE_END = ";";

	List<Line> provide();

	static LineProvider ofString(String string) {
		return new StringLineProvider(string);
	}
}
