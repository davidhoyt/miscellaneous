package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Potential test improvements:
 *
 * <ol>
 *     <li>Test out of order method calls.</li>
 * </ol>
 */
public class TFlySimpleClientTests {
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
     * Connects to a local server and disconnects immediately.
     */
	@Test
	public void testSimpleConnection() throws InterruptedException {
        final Semaphore sem_client_001 = new Semaphore(0);

        //Create a client
        final TFlySimpleClient client_001 = TFlySimpleClient.createLocal(new TFlySimpleClient.Callback() {
            @Override
            public void connected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
                sem_client_001.release();
            }

            @Override
            public void disconnected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
                sem_client_001.release();
            }
        });

        //Go ahead and connect
        assertNotNull(client_001.connect());

        //Make sure we can connect
        assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));

        //Validate that our API is indicating a valid connection
        assertTrue(client_001.isConnected());

        //Validate that our async handler has released a permit in the semaphore
        assertEquals(0, sem_client_001.availablePermits());

        //Disconnect -- should result in the client disconnected() method
        //called and the sem getting another permit.
        client_001.disconnect();

        assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));
        assertEquals(0, sem_client_001.availablePermits());

        //Validate that our API is indicating that we're not connected
        assertFalse(client_001.isConnected());
	}

    /**
     * Repeatedly connects and disconnects to a server using the same client instance.
     */
    @Test
    public void testClientReuse() throws InterruptedException {
        final Semaphore sem_client_001 = new Semaphore(0);

        //Create a client
        final TFlySimpleClient client_001 = TFlySimpleClient.createLocal(new TFlySimpleClient.Callback() {
            @Override
            public void connected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
                sem_client_001.release();
            }

            @Override
            public void disconnected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
                sem_client_001.release();
            }
        });

        for(int i = 0; i < 250; ++i) {
            assertNotNull(client_001.connect());
            assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));
            assertEquals(0, sem_client_001.availablePermits());
            assertTrue(client_001.isConnected());

            client_001.disconnect();
            assertTrue(sem_client_001.tryAcquire(1, 10L * 1000L, TimeUnit.MILLISECONDS));
            assertEquals(0, sem_client_001.availablePermits());
            assertFalse(client_001.isConnected());
        }

        //TODO: Check for CLOSE_WAIT
        //On unix systems you can issue the following command(s) from a terminal:
        //    netstat -ton | grep CLOSE_WAIT | wc -l
        //Should return 0 if you have no other sockets open on the system.
    }
}
