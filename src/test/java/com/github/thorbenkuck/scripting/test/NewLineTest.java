package com.github.thorbenkuck.scripting.test;

import com.github.thorbenkuck.scripting.parsing.Line;
import com.github.thorbenkuck.scripting.parsing.LineProvider;
import com.github.thorbenkuck.scripting.parsing.StringLineProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class NewLineTest {

    @Test
    public void test() {
        // Arrange
        String rawScript = "println(\"1\")"
                + "\r"
                + "\n"
                + "\r\n"
                + "println(\"2\")";
        LineProvider lineParser = new StringLineProvider(rawScript);

        // Act
        List<Line> provide = lineParser.provide();

        // Assert
        assertEquals(2, provide.size());
        assertEquals("println(\"1\")", provide.get(0).toString());
        assertEquals("println(\"2\")", provide.get(1).toString());

    }

}
