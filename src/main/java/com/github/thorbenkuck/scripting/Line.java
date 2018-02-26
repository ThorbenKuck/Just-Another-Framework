package com.github.thorbenkuck.scripting;

public class Line {

	private String content;

	public Line(String content) {
		this.content = content;
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

	@Override
	public String toString() {
		return content;
	}
}