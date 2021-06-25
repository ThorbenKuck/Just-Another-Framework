package com.github.thorbenkuck.scripting.parsing;

public interface LineParser extends Iterable<Line> {

	boolean hasNext();

	Line getNext();

	Line getCurrent();

	Line getLine(int lineNumber);

	void freezeLinePointer();

	void setLinePointer(int to);

	int getLinePointer();
}
