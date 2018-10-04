package com.github.thorbenkuck.scripting;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class DefaultLineParser implements LineParser {

	private final List<Line> lines = new ArrayList<>();
	// This should be -1 to
	// compensate the first
	// line we read. By starting
	// with -1, we increase to 0
	// within the first line.
	private final AtomicInteger linePointer = new AtomicInteger(-1);
	private final AtomicBoolean linePointerFreeze = new AtomicBoolean(false);

	DefaultLineParser() {
	}

	DefaultLineParser(String rawScript) {
		setLines(rawScript);
	}

	DefaultLineParser(LineProvider lineProvider) {
		setLines(lineProvider);
	}

	private List<Line> parseLines(LineProvider lineProvider) {
		return lineProvider.provide();
	}

	void setLines(LineProvider provider) {
		synchronized (lines) {
			this.lines.addAll(parseLines(provider));
		}
	}

	void setLines(String rawScript) {
		setLines(LineProvider.ofString(rawScript));
	}

	private void incrementLinePointer() {
		if (!linePointerFreeze.get()) {
			linePointer.incrementAndGet();
		}
	}

	public int getLineCount() {
		synchronized (lines) {
			return lines.size();
		}
	}

	private void unfreezeLinePointer() {
		linePointerFreeze.set(false);
	}

	@Override
	public Line getNext() {
		incrementLinePointer();
		unfreezeLinePointer();
		synchronized (lines) {
			return lines.get(linePointer.get());

		}
	}

	@Override
	public boolean hasNext() {
		boolean freeze = linePointerFreeze.get();
		if (freeze) {
			return getLinePointer() < getLineCount();
		}

		return getLinePointer() + 1 < getLineCount();
	}

	@Override
	public Line getCurrent() {
		synchronized (lines) {
			return lines.get(linePointer.get());
		}

	}

	@Override
	public Line getLine(int lineNumber) {
		synchronized (lines) {
			return lines.get(lineNumber);
		}
	}

	@Override
	public void freezeLinePointer() {
		linePointerFreeze.set(true);

	}

	@Override
	public void setLinePointer(int to) {
		int lineSize = getLineCount();
		if (to < 0 || lineSize < to) {
			throw new IllegalArgumentException("Line pointer has to be between " + 0 + " and " + lineSize + ". Provided: " + to);
		}
		synchronized (linePointer) {
			linePointer.set(to);
		}
	}

	@Override
	public int getLinePointer() {
		return linePointer.get();
	}

	@Override
	public Iterator<Line> iterator() {
		return new LineIterator(lines);
	}

	private final class LineIterator implements Iterator<Line> {

		private final Queue<Line> lines;

		private LineIterator(Collection<Line> lines) {
			this.lines = new LinkedList<>(lines);
		}

		@Override
		public boolean hasNext() {
			return lines.peek() != null;
		}

		@Override
		public Line next() {
			return lines.poll();
		}
	}
}
