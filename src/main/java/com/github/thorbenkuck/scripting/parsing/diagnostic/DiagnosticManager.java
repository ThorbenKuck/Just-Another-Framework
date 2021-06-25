package com.github.thorbenkuck.scripting.parsing.diagnostic;

import com.github.thorbenkuck.scripting.parsing.Line;

import java.io.PrintStream;

public interface DiagnosticManager {

	static DiagnosticManager systemOut() {
		return new PrintStreamDiagnosticManager(System.out);
	}

	static DiagnosticManager systemErr() {
		return new PrintStreamDiagnosticManager(System.err);
	}

	static DiagnosticManager toPrintStream(PrintStream printStream) {
		return new PrintStreamDiagnosticManager(printStream);
	}

	static CollectingDiagnosticManager collect() {
		return new CollectingDiagnosticManager();
	}

	void onError(String message, Line line);

	void onWarning(String message, Line line);

	void onNotice(String message, Line line);

}
