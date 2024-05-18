package org.socialnetworking.console;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleReader implements AutoCloseable {

    private final Scanner scanner;

    public ConsoleReader(InputStream inputStream) {
        scanner = new Scanner(inputStream);
    }


    public String readLine() {
        return scanner.nextLine();
    }


    @Override
    public void close() {
        scanner.close();
    }
}
