package org.socialnetworking.console;

import org.socialnetworking.CommandExecutor;
import org.socialnetworking.Repository;

import java.time.Clock;
import java.time.Instant;
import java.util.function.Supplier;

public class ConsoleApplication {


    private static final Supplier<Instant> TIME_SUPPLIER = () -> Instant.now(Clock.systemUTC());

    public static void main(String[] args) {
        try( var consoleReader = new ConsoleReader(System.in);
             var consoleWriter = new ConsoleWriter(System.out)) {

            var appRunner = new ApplicationRunner(
                    consoleReader,
                    consoleWriter,
                    new CommandExecutor(new Repository(), TIME_SUPPLIER),
                    TIME_SUPPLIER);

            appRunner.runApplication();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
