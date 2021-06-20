package nl.thijsalders.spigotproxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {


    private final ChannelInitializer<SocketChannel> oldChildHandler;
    private final Method oldChildHandlerMethod;
    private Field addr;

    public NettyChannelInitializer(ChannelInitializer<SocketChannel> oldChildHandler, String minecraftPackage) throws Exception {
        this.oldChildHandler = oldChildHandler;
        this.oldChildHandlerMethod = this.oldChildHandler.getClass().getDeclaredMethod("initChannel", Channel.class);
        this.oldChildHandlerMethod.setAccessible(true);

        Class<?> networkManager;
        try {
            networkManager = Class.forName("net.minecraft.network.NetworkManager");
        } catch (ClassNotFoundException e) {
            networkManager = Class.forName(minecraftPackage + ".NetworkManager");
        }
        try {
            this.addr = networkManager.getField("socketAddress");
        } catch (NoSuchFieldException e) {
            this.addr = networkManager.getField("l");
        }
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        this.oldChildHandlerMethod.invoke(this.oldChildHandler, channel);

        channel.pipeline().addAfter("timeout", "haproxy-decoder", new HAProxyMessageDecoder());
        channel.pipeline().addAfter("haproxy-decoder", "haproxy-handler", new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                if (msg instanceof HAProxyMessage) {
                    HAProxyMessage message = (HAProxyMessage) msg;
                    String realaddress = message.sourceAddress();
                    int realport = message.sourcePort();

                    SocketAddress socketaddr = new InetSocketAddress(realaddress, realport);

                    ChannelHandler handler = channel.pipeline().get("packet_handler");
                    addr.set(handler, socketaddr);
                } else {
                    super.channelRead(ctx, msg);
                }
            }
        });
    }

}
