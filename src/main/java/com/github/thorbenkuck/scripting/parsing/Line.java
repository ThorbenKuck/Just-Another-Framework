package com.github.thorbenkuck.scripting.parsing;

public interface Line extends Iterable<Character> {

	static Line create(String content, int lineNumber) {
		return new StringLine(content, lineNumber);
	}

	void remove(int index);

	void remove(int from, int to);

	void changeTo(String newLine);

	void trimLeadingWhiteSpaces();

	void trimWhiteSpaces();

	boolean matches(String regex);

	boolean contains(String s);

	boolean startsWith(String s);

	boolean isBlank();

	String toReadable();

	Line subPart(int begin);

	Line subPart(int begin, int end);

	int indexOf(String s);

	int lastIndexOf(String s);

	String[] split(String regex);

	int getLineNumber();

	Line duplicate();

	boolean isEmpty();

	char getAt(int index);

	void replace(String oldString, String newString);

	int length();

	boolean endsWith(String s);

	void removeLast();

	int countCharInLine(char toCount);

	void clear();
}
