package com.ticketfly;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A server that adheres to the conditions defined
 * <a href="https://github.com/Ticketfly/Platform-Engineer-Coding-Exercise">here</a>.
 *
 * It provides a fluent interface for interacting with the class.
 *
 * Assumptions:
 * <ol>
 *     <li>Requests end with a newline ('\n').</li>
 *     <li>The maximum size of an entire request, including the sequence number cannot exceed 10,240 bytes. This is to prevent too much memory consumption.</li>
 *     <li>The server will listen on all available interfaces.</li>
 *     <li>Based on the provided examples, the server maintains an open connection until a client explicitly closes it.</li>
 *     <li>A client can close a connection by sending an empty newline ('\n').</li>
 *     <li>The sequence number is shared among clients.</li>
 *     <li>The sequence number is shared among instances of this class.</li>
 *     <li>The sequence number is persisted between client connections.</li>
 *     <li>The sequence number is not persisted or shared between server processes or machines.</li>
 *     <li>The sequence number does not wrap around if it has reached its maximum. Behavior is undefined at that point.</li>
 *     <li>Requests can have digits in them, apart from the sequence number.</li>
 * </ol>
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public final class TFlySimpleServer {
    /**
     * The port this server should typically run on.
     */
    public static final int DEFAULT_SERVER_PORT = 4567;

    /**
     * The default for the maximum size (in bytes) of an entire request.
     */
    static final int DEFAULT_MAX_REQUEST_SIZE = 10240;

    /**
     * The style of newline used by this protocol. Note that is explicitly
     * NOT System.getProperty("line.separator") because our protocol should
     * behave identically across platforms.
     */
    static final String PROTOCOL_NEWLINE = "\n";

    /**
     * The delimiter(s) used to mark the end of a request.
     */
    static final ByteBuf[] PROTOCOL_DELIMITERS = new ByteBuf[] {
        Unpooled.wrappedBuffer(new byte[] { '\n' })
    };

    /**
     * Provides an instance of a {@link StringDecoder} that will be shared among instances of this class.
     */
    static final StringDecoder STRING_DECODER = new StringDecoder(Charset.forName("UTF-8"));

    /**
     * Provides an instance of a {@link StringEncoder} that will be shared among instances of this class.
     */
    static final StringEncoder STRING_ENCODER = new StringEncoder(Charset.forName("UTF-8"));

    /**
     * {@link Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(TFlySimpleServer.class.getName());

    /**
     * The port number this server will listen on.
     */
    private final int port;

    /**
     * The maximum size (in bytes) of any request to this server.
     */
    private final int maximum_request_size;

    /**
     * A lock used by the server to synchronize access to important operations
     * such as {@link #start()} and {@link #stop()}.
     */
    private final Object lock = new Object();

    /**
     * Indicates whether the server is currently running or not.
     */
    private boolean running = false;

    /**
     * A reference to the Netty {@link ServerBootstrap} that defines connection
     * parameters and channel pipelines.
     */
    private ServerBootstrap server_bootstrap;

    /**
     * Private constructor to prevent instantiation outside the class.
     *
     * Creates an instance of a server to listen for incoming requests from clients.
     *
     * @param port the port number this server will listen on.
     * @param maximum_request_size the maximum size (in bytes) of any request to this server.
     */
    private TFlySimpleServer(int port, int maximum_request_size) {
        this.port = port;
        this.maximum_request_size = maximum_request_size;
        this.server_bootstrap = new ServerBootstrap()
            .group(new NioEventLoopGroup(), new NioEventLoopGroup())
            .channel(new NioServerSocketChannel())
            .localAddress(port)
            .option(ChannelOption.SO_BACKLOG, 100)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .handler(new LoggingHandler(LogLevel.INFO))
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline
                        .addLast("framer", new DelimiterBasedFrameDecoder(DEFAULT_MAX_REQUEST_SIZE, false, true, PROTOCOL_DELIMITERS))
                        .addLast("decoder", STRING_DECODER)
                        .addLast("encoder", STRING_ENCODER)
                        .addLast("handler", new TFlySimpleServerHandler())
                    ;
                }
            })
        ;
    }

    /**
     * Instantiates an instance of {@link TFlySimpleServer}, but is
     * not automatically started until {@link TFlySimpleServer#start()} is
     * called.
     *
     * @return a newly initialized instance of {@link TFlySimpleServer}.
     */
    public static TFlySimpleServer create() {
        return create(DEFAULT_SERVER_PORT);
    }

    /**
     * Instantiates an instance of {@link TFlySimpleServer}, but is
     * not automatically started until {@link TFlySimpleServer#start()} is
     * called.
     *
     * @param port the port number the server will listen on.
     * @return a newly initialized instance of {@link TFlySimpleServer}.
     */
    public static TFlySimpleServer create(int port) {
        return new TFlySimpleServer(port, DEFAULT_MAX_REQUEST_SIZE);
    }

    /**
     * The port number the server is or will listen on.
     *
     * @return an integer representing the server's port number.
     */
    public int getPort() {
        return port;
    }

    /**
     * The maximum request size (in bytes) the server will accept.
     *
     * @return an integer representing the server's maximum accepted request size (in bytes).
     */
    public int getMaximumRequestSize() {
        return maximum_request_size;
    }

    /**
     * Indicates if the server has been started successfully.
     *
     * @return true if the server is started and listening on the provided port ({@link #getPort()})
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Opens a socket on the provided port ({@link #getPort()}).
     *
     * @return the current {@link TFlySimpleServer} instance with a
     *         socket initialized and listening on the provided port
     *         ({@link #getPort()})
     */
    public TFlySimpleServer start() {
        try {
            synchronized(lock) {
                server_bootstrap.bind().sync();
                running = true;
            }
        } catch(Throwable t) {
            logger.log(Level.WARNING, "Error starting the server: " + t.getMessage());
            server_bootstrap.shutdown();
        }
        return this;
    }

    /**
     * Closes the socket the server is listening on.
     *
     * @return the current {@link TFlySimpleServer} instance
     */
    public TFlySimpleServer stop() {
        synchronized(lock) {
            if (running) {
                try {
                    server_bootstrap.shutdown();
                    running = false;
                } catch(Throwable t) {
                    logger.log(Level.WARNING, "Error stopping the server", t);
                }
            }
        }
        return this;
    }
}
