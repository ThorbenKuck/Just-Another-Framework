package com.github.thorbenkuck.scripting.parsing.diagnostic;

import com.github.thorbenkuck.scripting.parsing.Line;

public class DiagnosticEntry {

    private final DiagnosticLevel level;
    private final String message;
    private final Line line;

    public DiagnosticEntry(DiagnosticLevel level, String message, Line line) {
        this.level = level;
        this.message = message;
        this.line = line;
    }

    public DiagnosticLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public Line getLine() {
        return line;
    }
}
