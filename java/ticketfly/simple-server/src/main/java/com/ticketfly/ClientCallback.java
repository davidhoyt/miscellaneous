package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;

/**
 * Defines a callback when a connection has been established between
 * two endpoints, when data is exchanged, and when that connection has
 * been terminated.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public interface ClientCallback<TSource, TData> {
    void connected(TSource source, ChannelHandlerContext context) throws InterruptedException;
    void dataReceived(TSource source, TData data) throws InterruptedException;
    void disconnected(TSource source, ChannelHandlerContext context) throws InterruptedException;
}
