package com.github.thorbenkuck.scripting.parsing;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringLineProvider implements LineProvider {

	private final String inserted;

	public StringLineProvider(final String inserted) {
		this.inserted = inserted;
	}

	@Override
	public List<Line> provide() {
		List<Line> lines = new ArrayList<>();

		String[] splitLines = inserted.split("\\R");

		for(String currentLine : splitLines) {
			StringBuilder complete = new StringBuilder(currentLine);
			int lineNumber = 0;
			while (! complete.toString().isEmpty()) {
				if(currentLine.isEmpty()) {
					continue;
				}

				String line;
				if (! complete.toString().contains(LINE_END)) {
					line = complete.toString();
				} else {
					line = complete.substring(0, complete.indexOf(LINE_END));
					complete.deleteCharAt(complete.indexOf(LINE_END));
				}
				while (line.startsWith(" ")) {
					line = line.substring(1);
					complete.deleteCharAt(0);
				}

				if (! line.isEmpty()) {
					lines.add(Line.create(line, lineNumber++));
				}
				complete.delete(0, line.length());
			}
		}

		return lines;
	}
}
