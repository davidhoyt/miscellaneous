package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Utility class for interacting with a {@link TFlySimpleServer} server.
 */
public class TFlySimpleServerTestUtil {
    /**
     * Used to launch a single client against a server, send message(s), and then validate the response(s) to those message(s).
     */
    public static void validateMessages(final TFlySimpleServer server, final String[] messages_to_server, final String[] responses_from_server) throws InterruptedException {
        final Semaphore sem_client_001 = new Semaphore(0);
        final Semaphore sem_client_002 = new Semaphore(0);

        if (messages_to_server.length != responses_from_server.length) {
            throw new IllegalArgumentException("Messages and responses must be the same size");
        }

        //Start the server
        final TFlySimpleClient client_001 = TFlySimpleClient.create("localhost", server.getPort(), new TFlySimpleClient.Callback() {
            int counter = 0;

            @Override
            public void connected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
                sem_client_001.release();
            }

            @Override
            public void disconnected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
                sem_client_001.release();
            }

            @Override
            public void dataReceived(TFlySimpleClient tFlySimpleClient, String msg) throws InterruptedException {
                msg = msg.trim();
                if ("".equals(msg)) {
                    return;
                }

                assertEquals(responses_from_server[counter], msg);
                ++counter;
                sem_client_002.release();
            }
        });

        client_001.connect();
        sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS);

        for(String msg : messages_to_server) {
            client_001.write(msg);
            sem_client_002.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS);
        }

        client_001.disconnect();
        sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * Simple wrapper to create an array out of a var arg message list.
     */
    public static String[] messagesToServer(String...messages) {
        return messages;
    }

    /**
     * Simple wrapper to create an array out of a var arg expected response list.
     */
    public static String[] responsesFromServer(String...responses) {
        return responses;
    }
}
