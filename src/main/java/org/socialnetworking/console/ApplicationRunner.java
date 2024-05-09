package org.socialnetworking.console;

import org.socialnetworking.CommandExecutor;
import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.OutputMessage;

public class ApplicationRunner {

    private ConsoleReader consoleReader;
    private ConsoleWriter consoleWriter;
    private CommandExecutor commandExecutor;


    public ApplicationRunner(ConsoleReader consoleReader, ConsoleWriter consoleWriter, CommandExecutor commandExecutor) {
        this.consoleReader = consoleReader;
        this.consoleWriter = consoleWriter;
        this.commandExecutor = commandExecutor;
    }

    public void runApplication() {
        for (var input = readLine(); isNotQuit(input); input = readLine()) {
            var outputMessage = commandExecutor.execute(CommandEntered.parse(input));
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
