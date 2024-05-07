package org.socialnetworking.console;

import java.io.*;

public class ConsoleWriter implements AutoCloseable {
    private final PrintStream outputStream;

    public ConsoleWriter(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeLine(String line) {
        outputStream.println(line);
    }

    @Override
    public void close() {
        outputStream.close();
    }
}
