package org.ebudosky;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


class AnalyticalToolTest {
    private AnalyticalTool tool;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        tool = new AnalyticalTool();
        System.setOut(new PrintStream(outputStreamCaptor));
        seedBaseData();
    }

    private void seedBaseData() {
        // Service 1 Hierarchy
        tool.addRecord("C 1.1 8.15.1 P 15.10.2012 83"); // Service 1.1, Question 8.15.1
        tool.addRecord("C 1 10.1 P 01.12.2012 65");     // Service 1 (no variation), Question 10.1

        // Service 1.1 again for range/average testing
        tool.addRecord("C 1.1 5.5.1 P 01.11.2012 117"); // Service 1.1, Question 5.5.1

        // Different Service and Response Type
        tool.addRecord("C 3 10.2 N 02.10.2012 100");    // Service 3, Question 10.2
    }

    private String getOutput() {
        String out = outputStreamCaptor.toString().trim();
        outputStreamCaptor.reset(); // Clear after reading for the next test
        return out;
    }

    @Test
    @DisplayName("Question parent matches the child")
    void testParentChildQuestionMatch() {
        // Query for 10 matches 10.1 and 10.2, but response type 'P' filters it to only 10.1
        tool.executeQuery("D 1 10 P 01.10.2012-31.12.2012");
        assertEquals("65", getOutput());
    }

    @Test
    @DisplayName("Question parent matches the grandchild")
    void testParentGrandchildQuestionHierarchy() {
        // Query for '8' should match '8.15.1'
        tool.executeQuery("D 1.1 8 P 01.10.2012-01.12.2012");
        assertEquals("83", getOutput(), "Parent question query should match grandchild record");

    }

    @Test
    @DisplayName("Question child matches the grandchild")
    void testChildGrandchildQuestionHierarchy() {
        // Query for '8.15' should match '8.15.1'
        tool.executeQuery("D 1.1 8.15 P 01.10.2012-01.12.2012");
        assertEquals("83", getOutput(), "Child question query should match grandchild record");
    }

    @Test
    @DisplayName("Exact deepest possible match")
    void testExactDeepestPossibleMatch() {
        // The exact same record is present
        tool.executeQuery("D 1.1 5.5.1 P 01.11.2012");
        assertEquals("117", getOutput());
    }

    @Test
    @DisplayName("Full mismatch logic")
    void testAbsoluteMismatch() {
        // Service matches (1), Answer Type matches (P), but Question type (9) exists nowhere
        tool.executeQuery("D 1 9 P 01.01.2012-31.12.2012");
        assertEquals("-", getOutput());
    }

    @Test
    @DisplayName("Service parent matches child")
    void testServiceVariationMatching() {
        tool.executeQuery("D 1 * P 14.10.2012-01.12.2012");
        assertEquals("88", getOutput());
    }

    @Test
    @DisplayName("Wildcard for service and question")
    void testGlobalWildcards() {
        // All 'P' records: 83, 65, 117. Sum = 265. Avg = 88.33
        tool.executeQuery("D * * P 01.01.2012-31.12.2012");
        assertEquals("88", getOutput());
    }

    @Test
    @DisplayName("Query deeper than Record")
    void testDeepQueryOnShallowRecord() {
        tool.executeQuery("D 1.1 10.1 P 01.12.2012");
        assertEquals("-", getOutput());
    }
}