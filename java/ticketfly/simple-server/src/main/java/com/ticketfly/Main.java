package com.ticketfly;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.in;
import static java.lang.System.out;

/**
 * Launches a server.
 *
 * It would be nice to accept the port number as a command line argument and it
 * would be trivial to add. For now, the port number corresponds to
 * TFlySimpleServer.DEFAULT_SERVER_PORT.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.log(Level.INFO, "Creating the server");
        final TFlySimpleServer server = TFlySimpleServer.create(TFlySimpleServer.DEFAULT_SERVER_PORT);

        logger.log(Level.INFO, "Starting the server.");
        server.start();

        if (server.isRunning()) {
            logger.log(Level.INFO, "Server started.");

            out.println("Press <enter> to stop the server and exit.");

            try {
                in.read();
            } catch(IOException e) {
                logger.log(Level.WARNING, "Error", e);
            }
        } else {
            logger.log(Level.SEVERE, "Server unable to start.");
        }

        logger.log(Level.INFO, "Stopping the server.");
        server.stop();

        logger.log(Level.INFO, "Server stopped.");

        System.exit(0);
    }
}
