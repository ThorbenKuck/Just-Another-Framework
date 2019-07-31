package com.github.thorbenkuck.scripting.exceptions;

public class IllegalByteCodeException extends RuntimeException {

	public IllegalByteCodeException() {
	}

	public IllegalByteCodeException(String message) {
		super(message);
	}

	public IllegalByteCodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalByteCodeException(Throwable cause) {
		super(cause);
	}

	public IllegalByteCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
