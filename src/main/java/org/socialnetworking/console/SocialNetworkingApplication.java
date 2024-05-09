package org.socialnetworking.console;

import org.socialnetworking.CommandExecutor;
import org.socialnetworking.Repository;

import java.time.Clock;
import java.time.Instant;

public class SocialNetworkingApplication {


    public static void main(String[] args) {
        try( var consoleReader = new ConsoleReader(System.in);
             var consoleWriter = new ConsoleWriter(System.out)) {

            var appRunner = new ApplicationRunner(
                    consoleReader,
                    consoleWriter,
                    new CommandExecutor(new Repository(), (() -> Instant.now(Clock.systemUTC()))));

            appRunner.runApplication();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
