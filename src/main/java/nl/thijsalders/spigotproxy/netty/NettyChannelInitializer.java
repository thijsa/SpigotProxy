package nl.thijsalders.spigotproxy.netty;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import nl.thijsalders.spigotproxy.haproxy.HAProxyMessage;
import nl.thijsalders.spigotproxy.haproxy.HAProxyMessageDecoder;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	
	private ChannelInitializer<SocketChannel> oldChildHandler;
	private Method oldChildHandlerMethod;
	private Field addr;
	
	public NettyChannelInitializer(ChannelInitializer<SocketChannel> oldChildHandler, String minecraftPackage) throws Exception {
		this.oldChildHandler = oldChildHandler;
		this.oldChildHandlerMethod = this.oldChildHandler.getClass().getDeclaredMethod("initChannel", Channel.class);
		this.oldChildHandlerMethod.setAccessible(true);

		Class<?> networkManager = Class.forName(minecraftPackage + ".NetworkManager");
		this.addr = networkManager.getField("l");
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		this.oldChildHandlerMethod.invoke(this.oldChildHandler, channel);
		
		channel.pipeline().addAfter("timeout", "haproxy-decoder", new HAProxyMessageDecoder());
		channel.pipeline().addAfter("haproxy-decoder", "haproxy-handler", new ChannelInboundHandlerAdapter(){
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