package com.github.thorbenkuck.scripting.exceptions;

import java.util.Collections;
import java.util.List;

public class ParsingFailedException extends Exception {

	private final List<Integer> lineNumbers;

	public ParsingFailedException(String message, int lineNumber) {
		this(message, Collections.singletonList(lineNumber));
	}

	public ParsingFailedException(String message, Throwable cause, int lineNumber) {
		this(message, cause, Collections.singletonList(lineNumber));
	}

	public ParsingFailedException(String message, List<Integer> lineNumbers) {
		super(message);
		this.lineNumbers = lineNumbers;
	}

	public ParsingFailedException(String message, Throwable cause, List<Integer> lineNumbers) {
		super(message, cause);
		this.lineNumbers = lineNumbers;
	}

	public ParsingFailedException(List<Integer> lineNumbers) {
		this.lineNumbers = lineNumbers;
	}

	public ParsingFailedException(Throwable cause, List<Integer> lineNumbers) {
		super(cause);
		this.lineNumbers = lineNumbers;
	}

	public List<Integer> getLineNumbers() {
		return lineNumbers;
	}
}
