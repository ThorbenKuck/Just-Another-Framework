package com.github.thorbenkuck.scripting;

class DefaultDiagnosticManager implements DiagnosticManager {
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
		System.out.println(prefix + " > " + line.getLineNumber() + ": " + message);
		System.out.println(line.toReadable());
	}
}
