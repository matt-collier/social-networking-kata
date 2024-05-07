package org.socialnetworking.console;

import org.socialnetworking.CommandExecutor;
import org.socialnetworking.Repository;
import org.socialnetworking.domain.CommandEntered;
import org.socialnetworking.domain.OutputMessage;

public class ApplicationRunner {

    private final ConsoleReader consoleReader;
    private final ConsoleWriter consoleWriter;
    private final CommandExecutor commandExecutor;


    public ApplicationRunner(final ConsoleReader consoleReader, final ConsoleWriter consoleWriter, final Repository repository) {
        this.consoleReader = consoleReader;
        this.consoleWriter = consoleWriter;
        this.commandExecutor = new CommandExecutor(repository);
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

    private boolean isNotQuit(final String input) {
        return !input.equalsIgnoreCase("Q");
    }

    private void writeOutput(final OutputMessage outputMessage) {
        outputMessage.lines().forEach(consoleWriter::writeLine);
    }
}
