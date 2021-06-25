package com.github.thorbenkuck.scripting.parsing.diagnostic;

import com.github.thorbenkuck.scripting.parsing.Line;

import java.util.ArrayList;
import java.util.List;

public class CollectingDiagnosticManager implements DiagnosticManager {

    private final List<DiagnosticEntry> values = new ArrayList<>();

    @Override
    public void onError(String message, Line line) {
        values.add(new DiagnosticEntry(DiagnosticLevel.ERROR, message, line));
    }

    @Override
    public void onWarning(String message, Line line) {
        values.add(new DiagnosticEntry(DiagnosticLevel.WARN, message, line));
    }

    @Override
    public void onNotice(String message, Line line) {
        values.add(new DiagnosticEntry(DiagnosticLevel.NOTICE, message, line));
    }

    public List<DiagnosticEntry> drain() {
        List<DiagnosticEntry> copy = new ArrayList<>(values);
        values.clear();
        return copy;
    }

}
