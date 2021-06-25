package com.github.thorbenkuck.scripting.parsing.diagnostic;

import com.github.thorbenkuck.scripting.parsing.Line;

import java.io.PrintStream;

public class PrintStreamDiagnosticManager implements DiagnosticManager {

	private final PrintStream printStream;

	public PrintStreamDiagnosticManager(PrintStream printStream) {
		this.printStream = printStream;
	}

	@Override
	public void onError(String message, Line line) {
		print("[error]", message, line);
	}

	@Override
	public void onWarning(String message, Line line) {
		print("[warn]", message, line);
	}

	@Override
	public void onNotice(String message, Line line) {
		print("[note]", message, line);
	}

	private void print(String prefix, String message, Line line) {
		printStream.println(prefix + " > " + line.getLineNumber() + ": " + message);
		printStream.println(line.toReadable());
	}
}
