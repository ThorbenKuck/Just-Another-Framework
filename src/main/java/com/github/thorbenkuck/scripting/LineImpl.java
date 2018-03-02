package com.github.thorbenkuck.scripting;

import java.util.*;

class LineImpl implements Line {

	private String content;
	private final int lineNumber;

	LineImpl(String content, int lineNumber) {
		this.content = content;
		this.lineNumber = lineNumber;
	}

	@Override
	public void remove(int index) {
		StringBuilder stringBuilder = new StringBuilder(content);
		stringBuilder.deleteCharAt(index);
		content = stringBuilder.toString();
	}

	@Override
	public void remove(int from, int to) {
		for(int index = from ; index <= to ; index++) {
			remove(0);
		}
	}

	@Override
	public void changeTo(String newLine) {
		this.content = newLine;
	}

	@Override
	public void trimLeadingWhiteSpaces() {
		while(content.startsWith(" ")) {
			content = content.replace(" ", "");
		}
	}

	@Override
	public void trimWhiteSpaces() {
		while(content.contains(" ")) {
			content = content.replace(" ", "");
		}
	}

	@Override
	public boolean matches(String regex) {
		return content.matches(regex);
	}

	@Override
	public boolean contains(String s) {
		return content.contains(s);
	}

	@Override
	public boolean startsWith(String s) {
		return content.startsWith(s);
	}

	@Override
	public boolean isBlank() {
		for(char character : content.toCharArray()) {
			if(!(character == ' ')) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toReadable() {
		return "(" +lineNumber + ") " + toString();
	}

	@Override
	public String toString() {
		return content;
	}

	@Override
	public Line subpart(int begin) {
		return subpart(begin, content.length());
	}

	@Override
	public Line subpart(int begin, int end) {
		return new LineImpl(content.substring(begin, end), lineNumber);
	}

	@Override
	public int indexOf(String s) {
		return content.indexOf(s);
	}

	@Override
	public int lastIndexOf(String s) {
		return content.lastIndexOf(s);
	}

	@Override
	public String[] split(String regex) {
		return content.split(regex);
	}

	@Override
	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public Line duplicate() {
		return new LineImpl(content, lineNumber);
	}

	@Override
	public boolean isEmpty() {
		return content.isEmpty();
	}

	@Override
	public char getAt(int index) {
		return content.toCharArray()[index];
	}

	@Override
	public void replace(String oldString, String newString) {
		content = content.replace(oldString, newString);
	}

	@Override
	public int length() {
		return content.length();
	}

	@Override
	public boolean endsWith(String s) {
		return content.endsWith(s);
	}

	@Override
	public void removeLast() {
		remove(content.length() - 1);
	}

	@Override
	public int countCharInLine(char toCount) {
		int count = 0;
		for (int i=0; i < content.length(); i++) {
			if (content.charAt(i) == toCount) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Character> iterator() {
		return new CharacterIterator(content.toCharArray());
	}

	private class CharacterIterator implements Iterator<Character> {

		private final Queue<Character> elements;
		private Character current;

		private CharacterIterator(final Collection<Character> elements) {
			this.elements = new LinkedList<>(elements);
		}

		private CharacterIterator(final char[] elements) {
			final List<Character> ourList = new ArrayList<>();

			for(char currentChar : elements) {
				ourList.add(currentChar);
			}

			this.elements = new LinkedList<>(ourList);
		}

		/**
		 * Returns {@code true} if the iteration has more elements.
		 * (In other words, returns {@code true} if {@link #next} would
		 * return an element rather than throwing an exception.)
		 *
		 * @return {@code true} if the iteration has more elements
		 */
		@Override
		public boolean hasNext() {
			return elements.peek() != null;
		}

		/**
		 * Returns the next element in the iteration.
		 *
		 * @return the next element in the iteration
		 * @throws NoSuchElementException if the iteration has no more elements
		 */
		@Override
		public Character next() {
			current = elements.poll();
			return current;
		}
	}
}