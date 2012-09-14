package com.ticketfly;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * Class used to connect to a {@link TFlySimpleServer} on the network.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public final class TFlySimpleClient {
    /**
     * The port the server should typically runs on.
     */
    public static final int DEFAULT_SERVER_PORT = TFlySimpleServer.DEFAULT_SERVER_PORT;

    /**
     * The default for the maximum size (in bytes) of an entire server response.
     */
    private static final int DEFAULT_MAX_RESPONSE_SIZE = TFlySimpleServer.DEFAULT_MAX_REQUEST_SIZE;

    /**
     * The delimiter(s) used to mark the end of a request/response.
     */
    private static final ByteBuf[] PROTOCOL_DELIMITERS = TFlySimpleServer.PROTOCOL_DELIMITERS;

    /**
     * The style of newline used by this protocol. Note that is explicitly
     * NOT System.getProperty("line.separator") because our protocol should
     * behave identically across platforms.
     */
    private static final String PROTOCOL_NEWLINE = TFlySimpleServer.PROTOCOL_NEWLINE;

    /**
     * Provides an instance of a {@link io.netty.handler.codec.string.StringDecoder} that will be shared among instances of this class.
     */
    private static final StringDecoder STRING_DECODER = TFlySimpleServer.STRING_DECODER;

    /**
     * Provides an instance of a {@link io.netty.handler.codec.string.StringEncoder} that will be shared among instances of this class.
     */
    private static final StringEncoder STRING_ENCODER = TFlySimpleServer.STRING_ENCODER;

    /**
     * Handles threading for all clients.
     */
    private static final NioEventLoopGroup EVENT_GROUP = new NioEventLoopGroup();

    /**
     * The port number to connect to the server on.
     */
    private final int port;

    /**
     * The host name of the server to connect to.
     */
    private final String host;

    /**
     * Flag used to hold the current connected status for this client.
     */
    private boolean connected = false;

    /**
     * Holds a reference to the connected {@link ChannelFuture}.
     */
    private volatile ChannelFuture connected_future = null;

    /**
     * A lock used by the client to synchronize access to important operations.
     */
    private final Object lock = new Object();

    /**
     * Holds a cached {@link InetSocketAddress} instance for use when
     * establishing a connection with the server.
     */
    private final InetSocketAddress remote_address;

    /**
     * Holds a reusable {@link ChannelHandler} instance for when client
     * {@link Bootstrap} instances need to be recreated.
     */
    private final ChannelHandler channel_handler;

    /**
     * The cached {@link Channel} instance used to make writes after the
     * client has connected.
     */
    private Channel channel;

    /**
     * A reference to the Netty {@link io.netty.bootstrap.Bootstrap} that defines connection
     * parameters and channel pipelines.
     */
    private Bootstrap client_bootstrap;

    /**
     * Private constructor to prevent instantiation outside the static create methods.
     */
    private TFlySimpleClient(String host, int port, final ClientCallback<TFlySimpleClient, String> callback) {
        this.host = host;
        this.port = port;
        this.remote_address = new InetSocketAddress(host, port);
        this.channel_handler = new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline
                    .addLast("framer", new DelimiterBasedFrameDecoder(DEFAULT_MAX_RESPONSE_SIZE, false, true, PROTOCOL_DELIMITERS))
                    .addLast("decoder", STRING_DECODER)
                    .addLast("encoder", STRING_ENCODER)
                    .addLast("handler", new TFlySimplyClientHandler(TFlySimpleClient.this, callback,
                        new CrossCallback() {
                            @Override
                            public void callback() {
                                connected = true;
                            }
                        },
                        new CrossCallback() {
                            @Override
                            public void callback() {
                                connected = false;
                                client_bootstrap = null;
                                connected_future = null;
                                channel = null;
                            }
                        }
                    ))
                ;
            }
        };
    }

    /**
     * Creates a newly initialized client ready to be connected to a local {@link TFlySimpleServer} server.
     *
     * @return a newly initialized instance of {@link TFlySimpleClient}
     */
    public static TFlySimpleClient createLocal(ClientCallback<TFlySimpleClient, String> callback) {
        return new TFlySimpleClient("localhost", DEFAULT_SERVER_PORT, callback);
    }

    /**
     * Creates a newly initialized client ready to be connected.
     *
     * @param host the host name of the server to connect to
     * @return a newly initialized instance of {@link TFlySimpleClient}
     */
    public static TFlySimpleClient create(String host, ClientCallback<TFlySimpleClient, String> callback) {
        return new TFlySimpleClient(host, DEFAULT_SERVER_PORT, callback);
    }

    /**
     * Creates a newly initialized client ready to be connected.
     *
     * @param host the host name of the server to connect to
     * @param port the port the server is listening on
     * @return a newly initialized instance of {@link TFlySimpleClient}
     */
    public static TFlySimpleClient create(String host, int port, ClientCallback<TFlySimpleClient, String> callback) {
        return new TFlySimpleClient(host, port, callback);
    }

    /**
     * The port number the client is or will connect on.
     *
     * @return an integer representing the endpoint's port number to connect to.
     */
    public int getPort() {
        return port;
    }

    /**
     * The server's host name.
     *
     * @return a string representing the server's host name.
     */
    public String getHost() {
        return host;
    }

    /**
     * Describes if the client is currently connected to a server.
     *
     * @return true if the client is currently connected to a server.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Opens a connection to a {@link TFlySimpleServer} server.
     *
     * @return an instance of {@link ChannelFuture} that allows interested parties
     *         to cancel or modify the action.
     */
    public ChannelFuture connect() {
        synchronized (lock) {
            if (isConnected())
                return connected_future;

            this.client_bootstrap = new Bootstrap()
                .group(EVENT_GROUP)
                .handler(channel_handler)
                .remoteAddress(remote_address)
                .channel(channel = new NioSocketChannel())
            ;

            return (connected_future = client_bootstrap.connect());
        }
    }

    /**
     * Disconnects from a {@link TFlySimpleServer} server.
     */
    public void disconnect() {
        synchronized (lock) {
            if (!isConnected() || client_bootstrap == null)
                return;
            channel.write(PROTOCOL_NEWLINE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * Writes a message according to established protocol to the server.
     * @param message A string that will come back from the server reversed.
     * @return an instance of {@link ChannelFuture} that allows interested parties
     *         to cancel or modify the action.
     */
    public ChannelFuture write(String message) {
        ChannelFuture future = channel.write(message + PROTOCOL_NEWLINE);
        channel.flush();
        return  future;
    }

    /**
     * Writes a message according to established protocol to the server.
     * @param message a string that will come back from the server reversed.
     * @param sequence_number a number to attempt to override the sequence number
     *                        on the server. This may be ignored if it's less than
     *                        the server's current sequence number.
     * @return an instance of {@link ChannelFuture} that allows interested parties
     *         to cancel or modify the action.
     */
    public ChannelFuture write(String message, int sequence_number) {
        if (sequence_number <= 0) {
            throw new IllegalArgumentException("Sequence numbers must be greater than 0");
        }
        ChannelFuture future = channel.write(message + " " + sequence_number + PROTOCOL_NEWLINE);
        channel.flush();
        return future;
    }

    public static class Callback implements ClientCallback<TFlySimpleClient, String> {
        @Override
        public void connected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
        }

        @Override
        public void dataReceived(TFlySimpleClient tFlySimpleClient, String s) throws InterruptedException {
        }

        @Override
        public void disconnected(TFlySimpleClient tFlySimpleClient, ChannelHandlerContext context) throws InterruptedException {
        }
    }
}
