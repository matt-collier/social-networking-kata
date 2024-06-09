package org.socialnetworking.console;

import org.socialnetworking.CommandExecutor;
import org.socialnetworking.domain.*;

import java.time.Instant;
import java.util.function.Supplier;

public class ApplicationRunner {

    private final ConsoleReader consoleReader;
    private final ConsoleWriter consoleWriter;
    private final CommandExecutor commandExecutor;
    private final Supplier<Instant> eventTimeSupplier;



    public ApplicationRunner(ConsoleReader consoleReader, ConsoleWriter consoleWriter, CommandExecutor commandExecutor, final Supplier<Instant> eventTimeSupplier) {
        this.consoleReader = consoleReader;
        this.consoleWriter = consoleWriter;
        this.commandExecutor = commandExecutor;
        this.eventTimeSupplier = eventTimeSupplier;
    }

    public void runApplication() {
        for (var input = readLine(); isNotQuit(input); input = readLine()) {
            var response = commandExecutor.execute(CommandParser.commandFor(input));
            writeOutput(outputMessageFor(response));
        }
    }

    private OutputMessage outputMessageFor(final Response response) {
        return new ConsoleView().outputFor(response, currentTime());
    }

    private Instant currentTime() {
        return eventTimeSupplier.get();
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
