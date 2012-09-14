package com.ticketfly;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

/**
 * Handles data processing from/to a {@link TFlySimpleServer} server.
 *
 * @author David Hoyt &lt;dhoyt@hoytsoft.org&gt;
 */
public class TFlySimplyClientHandler extends ChannelInboundMessageHandlerAdapter<String> {
    private TFlySimpleClient client;
    private CrossCallback on_connect, on_disconnect;
    private ClientCallback<TFlySimpleClient, String> callback;

    public TFlySimplyClientHandler(TFlySimpleClient client, ClientCallback<TFlySimpleClient, String> callback, CrossCallback on_connect, CrossCallback on_disconnect) {
        this.client = client;
        this.callback = callback;
        this.on_connect = on_connect;
        this.on_disconnect = on_disconnect;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        on_connect.callback();
        if (callback != null) {
            callback.connected(client, ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        on_disconnect.callback();
        if (callback != null) {
            callback.disconnected(client, ctx);
        }
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        if (callback != null) {
            callback.dataReceived(client, msg);
        }
    }
}
