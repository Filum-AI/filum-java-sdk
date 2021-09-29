package com.filum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FilumLogTest {
  private ByteArrayOutputStream outContent;
  private ByteArrayOutputStream errContent;
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;
  private final FilumLog filumLog = new FilumLog();

  @BeforeEach
  public void setUpStreams() {
    outContent = new ByteArrayOutputStream();
    errContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() throws IOException {
    outContent.close();
    errContent.close();
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @ParameterizedTest
  @MethodSource("logArguments")
  public void testLog(
      FilumLog.LogMode logMode,
      String expectedErrorLog,
      String expectedWarnLog,
      String expectedDebugLog) {
    filumLog.setLogMode(logMode);
    filumLog.error("Test", "error message");
    assertEquals(expectedErrorLog, errContent.toString().trim());
    filumLog.warn("Test", "warn message");
    assertEquals(expectedWarnLog, outContent.toString().trim());
    filumLog.log("Test", "debug message");
    assertEquals(expectedDebugLog, outContent.toString().trim());
  }

  static Stream<Arguments> logArguments() {
    return Stream.of(
        arguments(FilumLog.LogMode.ERROR, "Test: error message", "", ""),
        arguments(
            FilumLog.LogMode.WARN,
            "Test: error message",
            "Test: warn message",
            "Test: warn message"),
        arguments(
            FilumLog.LogMode.DEBUG,
            "Test: error message",
            "Test: warn message",
            "Test: warn message\nTest: debug message"),
        arguments(FilumLog.LogMode.OFF, "", "", ""));
  }
}
