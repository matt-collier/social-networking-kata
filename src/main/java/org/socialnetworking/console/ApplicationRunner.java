package org.socialnetworking.console;

import org.socialnetworking.CommandExecutor;
import org.socialnetworking.domain.CommandParser;
import org.socialnetworking.domain.OutputMessage;

public class ApplicationRunner {

    private final ConsoleReader consoleReader;
    private final ConsoleWriter consoleWriter;
    private final CommandExecutor commandExecutor;


    public ApplicationRunner(ConsoleReader consoleReader, ConsoleWriter consoleWriter, CommandExecutor commandExecutor) {
        this.consoleReader = consoleReader;
        this.consoleWriter = consoleWriter;
        this.commandExecutor = commandExecutor;
    }

    public void runApplication() {
        for (var input = readLine(); isNotQuit(input); input = readLine()) {
            var outputMessage = commandExecutor.execute(CommandParser.commandFor(input));
            writeOutput(outputMessage);
        }
    }

    private String readLine() {
        return consoleReader.readLine();
    }

    private boolean isNotQuit(String input) {
        return !input.equalsIgnoreCase("Q");
    }

    private void writeOutput(OutputMessage outputMessage) {
        outputMessage.lines().forEach(consoleWriter::writeLine);
    }
}
