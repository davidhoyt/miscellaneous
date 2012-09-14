package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

import java.net.BindException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Process incoming requests and is responsible for the behavior defined
 * <a href="https://github.com/Ticketfly/Platform-Engineer-Coding-Exercise">here</a>.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public class TFlySimpleServerHandler extends ChannelInboundMessageHandlerAdapter<String> {
    private static final Pattern REGEX_MATCH_REQUEST_AND_SEQ_NUMBER = Pattern.compile("([a-zA-Z0-9_\\ ]+)\\ ([0-9]+)([\\r\\n]+)");
    private static final Pattern REGEX_MATCH_REQUEST_ONLY = Pattern.compile("[a-zA-Z0-9_]+[\\r\\n]+");

    private static final Logger logger = Logger.getLogger(TFlySimpleServerHandler.class.getName());
    private static final AtomicInteger current_sequence_number = new AtomicInteger(0);

    /**
     * Executed upon client connect.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Client connected: " + ctx.channel());
    }

    /**
     * Executed upon client disconnect.
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.log(Level.INFO, "Client disconnected: " + ctx.channel());
    }

    /**
     * Asynchronously handles any exception thrown by netty.
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof BindException) {
            logger.log(Level.SEVERE, "Unable to bind to the port and start the server.");
            return;
        }

        logger.log(Level.WARNING, "Unexpected exception: ", cause);
        ctx.close();
    }

    /**
     * Executed when input has been received from a client.
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, String input) throws Exception {
        if (input.length() <= 0) {
            logger.log(Level.INFO, "Received zero length input from client: " + ctx.channel());
            ctx.write(ErrorCode.ERROR_MISSING_INPUT);
            return;
        }

        if ("".equals(input.trim())) {
            logger.log(Level.INFO, "Received empty line. Closing connection for : " + ctx.channel());
            ctx.close();
            return;
        }

        ctx.write(processInput(input));
    }

    /**
     * Please see the comments for {@link TFlySimpleServer} and {@link TFlySimpleServerHandler}
     * for details on approach and assumptions.
     *
     * @return a string representing the output we want to relay to the client
     */
    private String processInput(String input) {
        Matcher matcher;

        //Examples of possible input:
        //
        //    ticketfly   [output: ylftekcit 12]
        //    is_rad 789  [output: dar_si 790]
        //
        //A string, a space, and a number.
        //
        //We use regular expression pattern matching (pre-compiled) in
        //order to validate client input (request and sequence number).

        if ((matcher = REGEX_MATCH_REQUEST_AND_SEQ_NUMBER.matcher(input)) != null && matcher.matches() && matcher.groupCount() == 3) {
            //We have the following scenario:
            //    is_rad 789
            //We need to extract the integer on the end.

            int new_sequence_number;
            input = matcher.group(1) /*request*/ + matcher.group(3) /*newline*/;
            try {
                new_sequence_number = Integer.parseInt(matcher.group(2));
            } catch(Throwable t) {
                logger.log(Level.WARNING, "Invalid input: " + input);
                return ErrorCode.ERROR_INVALID_SEQUENCE_NUMBER.toString();
            }

            //Double check the validity of our sequence number.
            //It must be > 0 and greater than our current sequence number.
            //If it's not, we simply ignore it.
            if (new_sequence_number > 0 && new_sequence_number > current_sequence_number.get()) {
                current_sequence_number.set(new_sequence_number);
            } else {
                //If we receive an invalid sequence number, we log it and then ignore any
                //intended side effects.
                logger.log(Level.INFO, "Received a sequence number that wasn't applicable: " + new_sequence_number);
            }

        } else if ((matcher = REGEX_MATCH_REQUEST_ONLY.matcher(input)) != null && matcher.matches()) {

            //We have the following scenario:
            //    ticketfly
            input = matcher.group();

        } else {

            logger.log(Level.WARNING, "Invalid input: " + input);
            return ErrorCode.ERROR_INVALID_INPUT_FORMAT.toString();

        }

        //At this point, input contains just the request text and
        //current_sequence_number has either been updated at the client's
        //request or remains the same from the last invocation.

        //We now proceed to reverse the request, append a space, append
        //the sequence number, and then a couple of new lines in order
        //to mimic the example telnet session output.
        StringBuilder output = new StringBuilder(input).reverse();
        output.append(' ');
        output.append(current_sequence_number.incrementAndGet());
        output.append(TFlySimpleServer.PROTOCOL_NEWLINE);
        output.append(TFlySimpleServer.PROTOCOL_NEWLINE);

        return output.toString();
    }
}
