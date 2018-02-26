package com.github.thorbenkuck.scripting;

public class Line {

	private String content;
	private final int lineNumber;

	public Line(String content, int lineNumber) {
		this.content = content;
		this.lineNumber = lineNumber;
	}

	public void remove(int index) {
		StringBuilder stringBuilder = new StringBuilder(content);
		stringBuilder.deleteCharAt(index);
		content = stringBuilder.toString();
	}

	public void remove(int from, int to) {
		for(int index = from ; index <= to ; index++) {
			remove(0);
		}
	}

	public void changeTo(String newLine) {
		this.content = newLine;
	}

	public void trimLeadingWhiteSpaces() {
		while(content.startsWith(" ")) {
			content = content.replace(" ", "");
		}
	}

	public void trimWhiteSpaces() {
		while(content.contains(" ")) {
			content = content.replace(" ", "");
		}
	}

	public boolean matches(String regex) {
		return content.matches(regex);
	}

	public boolean contains(String s) {
		return content.contains(s);
	}

	public boolean startsWith(String s) {
		return content.startsWith(s);
	}

	public boolean isBlank() {
		for(char character : content.toCharArray()) {
			if(!(character == ' ')) {
				return false;
			}
		}
		return true;
	}

	public String toReadable() {
		return "(" +lineNumber + ") " + toString();
	}

	@Override
	public String toString() {
		return content;
	}

	public Line subpart(int begin) {
		return subpart(begin, content.length());
	}

	public Line subpart(int begin, int end) {
		return new Line(content.substring(begin, end), lineNumber);
	}

	public int indexOf(String s) {
		return content.indexOf(s);
	}

	public int lastIndexOf(String s) {
		return content.lastIndexOf(s);
	}

	public String[] split(String regex) {
		return content.split(regex);
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public Line duplicate() {
		return new Line(content, lineNumber);
	}

	public boolean isEmpty() {
		return content.isEmpty();
	}

	public char getAt(int index) {
		return content.toCharArray()[index];
	}

	public void replace(String oldString, String newString) {
		content = content.replace(oldString, newString);
	}

	public int length() {
		return content.length();
	}

	public boolean endsWith(String s) {
		return content.endsWith(s);
	}
}