package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Potential test improvements:
 *
 * <ol>
 *     <li>Increase the load and distribute across multiple machines to increase the parallelism of the tests.</li>
 *     <li>Run tests multiple times to decrease the amount of non-determinism inherit in the tests.</li>
 * </ol>
 */
public class TFlySimpleServerPerfTests {
    private static TFlySimpleServer server_001;

    @BeforeClass
    public static void beforeClass() {
        //Start the server
        server_001 = TFlySimpleServer.create().start();
        assertTrue(server_001.isRunning());
    }

    @AfterClass
    public static void afterClass() {
        //Stop the server
        server_001.stop();
        assertFalse(server_001.isRunning());
    }

    /**
     * Launches multiple clients to talk with the server all at once.
     */
    @Test
    public void testMultipleClients() throws InterruptedException {
        final int NUM_CLIENTS = 100;
        final int NUM_MSGS_PER_CLIENT = 100;
        final CountDownLatch latch_client_messages = new CountDownLatch(NUM_CLIENTS * NUM_MSGS_PER_CLIENT);
        final CountDownLatch latch_client_disconnect = new CountDownLatch(NUM_CLIENTS);
        final TFlySimpleClient[] clients = new TFlySimpleClient[NUM_CLIENTS];

        for(int i = 0; i < NUM_CLIENTS; ++i) {
            clients[i] = TFlySimpleClient.createLocal(new TFlySimpleClient.Callback() {

                @Override
                public void connected(TFlySimpleClient client, ChannelHandlerContext context) throws InterruptedException {
                }

                @Override
                public void disconnected(TFlySimpleClient client, ChannelHandlerContext context) throws InterruptedException {
                    latch_client_disconnect.countDown();
                }

                @Override
                public void dataReceived(TFlySimpleClient client, String msg) throws InterruptedException {
                    msg = msg.trim();
                    if ("".equals(msg)) {
                        return;
                    }

                    if (msg.startsWith("tset")) {
                        latch_client_messages.countDown();
                    }
                }
            });
            clients[i].connect();
        }

        for(int i = 0; i < NUM_MSGS_PER_CLIENT; ++i) {
            for(int j = 0; j < NUM_CLIENTS; ++j) {
                clients[j].write("test");
            }
        }

        assertTrue(latch_client_messages.await(10, TimeUnit.SECONDS));
        for(int i = 0; i < NUM_CLIENTS; ++i) {
            clients[i].disconnect();
        }

        assertTrue(latch_client_disconnect.await(10, TimeUnit.SECONDS));
    }
}
