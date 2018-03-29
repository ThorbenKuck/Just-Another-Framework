package com.github.thorbenkuck.scripting;

public interface DiagnosticManager {

	static DiagnosticManager createDefault() {
		return new DefaultDiagnosticManager();
	}

	void onError(String message, Line line);

	void onWarning(String message, Line line);

	void onNotice(String message, Line line);

}
