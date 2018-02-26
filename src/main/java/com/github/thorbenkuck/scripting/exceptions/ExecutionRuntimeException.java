package com.github.thorbenkuck.scripting.exceptions;

public class ExecutionRuntimeException extends RuntimeException {

	public ExecutionRuntimeException() {
	}

	public ExecutionRuntimeException(String message) {
		super(message);
	}

	public ExecutionRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExecutionRuntimeException(Throwable cause) {
		super(cause);
	}
}
