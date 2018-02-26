package com.github.thorbenkuck.scripting.exceptions;

public class ParsingFailedException extends Exception {

	public ParsingFailedException() {
	}

	public ParsingFailedException(String message) {
		super(message);
	}

	public ParsingFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParsingFailedException(Throwable cause) {
		super(cause);
	}
}
