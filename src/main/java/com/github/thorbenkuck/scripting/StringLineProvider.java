package com.github.thorbenkuck.scripting;

import java.util.ArrayList;
import java.util.List;

public class StringLineProvider implements LineProvider {

	private final String inserted;

	public StringLineProvider(final String inserted) {
		this.inserted = inserted;
	}

	@Override
	public List<Line> provide() {
		List<Line> lines = new ArrayList<>();
		StringBuilder complete = new StringBuilder(inserted);
		int lineNumber = 0;
		while (! complete.toString().isEmpty()) {
			String line;
			if (! complete.toString().contains(LINE_END)) {
				line = complete.toString();
			} else {
				line = complete.substring(0, complete.indexOf(LINE_END));
				complete.deleteCharAt(complete.indexOf(LINE_END));
			}
			while (line.startsWith(" ")) {
				line = line.substring(1, line.length());
				complete.deleteCharAt(0);
			}

			if (! line.isEmpty()) {
				lines.add(Line.create(line, lineNumber++));
			}
			complete.delete(0, line.length());
		}

		return lines;
	}
}
