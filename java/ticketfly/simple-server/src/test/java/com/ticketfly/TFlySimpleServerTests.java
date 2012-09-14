package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static com.ticketfly.TFlySimpleServerTestUtil.*;
import static org.junit.Assert.*;

/**
 * Potential test improvements:
 *
 * <ol>
 *     <li>Test out of order method calls.</li>
 *     <li>Send a message that exceeds the max request size buffer.</li>
 *     <li>Send a blank message.</li>
 *     <li>Send invalid data.</li>
 *     <li>Send data that cannot be UTF-8 decoded.</li>
 * </ol>
 */
public class TFlySimpleServerTests {
    /**
     * Creates, starts, and stops a single server.
     */
    @Test
    public void testSimpleServerCreation() {
        //Start the server
        final TFlySimpleServer server_001 = TFlySimpleServer.create().start();
        assertTrue(server_001.isRunning());

        //Stop the server
        server_001.stop();
        assertFalse(server_001.isRunning());
    }

    /**
     * Creates, starts, and stops a single server.
     */
    @Test
    public void testMultipleServersOnSameMachine() {
        //Start the server
        final TFlySimpleServer server_001 = TFlySimpleServer.create();
        final TFlySimpleServer server_002 = TFlySimpleServer.create(server_001.getPort());

        server_001.start();
        assertTrue(server_001.isRunning());

        server_002.start();
        assertFalse(server_002.isRunning());

        //Stop the server
        server_001.stop();
        assertFalse(server_001.isRunning());
    }

    /**
     * Creates, starts, and stops a single server.
     */
    @Test
    public void testSimpleClientCommunication() throws InterruptedException {
        final Semaphore sem_client_001 = new Semaphore(0);

        //Start the server
        final TFlySimpleServer server_001 = TFlySimpleServer.create();
        final TFlySimpleClient client_001 = TFlySimpleClient.createLocal(new TFlySimpleClient.Callback() {
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

                assertEquals("egassem_tset 1", msg);
                sem_client_001.release();
            }
        });

        server_001.start();
        assertTrue(server_001.isRunning());

        assertNotNull(client_001.connect());
        assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));

        client_001.write("test_message");
        assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));

        client_001.disconnect();
        assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));

        //Stop the server
        server_001.stop();
        assertFalse(server_001.isRunning());
    }

    /**
     * Sends some standard messages and looks for the appropriate response.
     */
    @Test
    public void testStandardClientCommunication() throws InterruptedException {
        final TFlySimpleServer server_001 = TFlySimpleServer.create().start();
        validateMessages(
            server_001,
            messagesToServer(
                  "test_message 2"
                , "test_message1"
                , "test_message2"
                , "test_message3 100"
                , "test_message4 0"
            ), responsesFromServer(
                  "egassem_tset 3"
                , "1egassem_tset 4"
                , "2egassem_tset 5"
                , "3egassem_tset 101"
                , "4egassem_tset 102"
            )
        );
        server_001.stop();
    }
}
