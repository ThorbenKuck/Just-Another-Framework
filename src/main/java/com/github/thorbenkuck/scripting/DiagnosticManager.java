package com.github.thorbenkuck.scripting;

import java.io.PrintStream;

public interface DiagnosticManager {

	static DiagnosticManager systemOut() {
		return new DefaultDiagnosticManager(System.out);
	}

	static DiagnosticManager systemErr() {
		return new DefaultDiagnosticManager(System.err);
	}

	static DiagnosticManager toPrintStream(PrintStream printStream) {
		return new DefaultDiagnosticManager(printStream);
	}

	void onError(String message, Line line);

	void onWarning(String message, Line line);

	void onNotice(String message, Line line);

}
