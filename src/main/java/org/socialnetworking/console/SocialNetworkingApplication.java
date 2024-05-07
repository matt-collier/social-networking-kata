package org.socialnetworking.console;

import org.socialnetworking.Repository;

public class SocialNetworkingApplication {


    public static void main(String[] args) {
        try( var consoleReader = new ConsoleReader(System.in);
             var consoleWriter = new ConsoleWriter(System.out)) {

            var appRunner = new ApplicationRunner(
                    consoleReader,
                    consoleWriter,
                    new Repository());

            appRunner.runApplication();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
