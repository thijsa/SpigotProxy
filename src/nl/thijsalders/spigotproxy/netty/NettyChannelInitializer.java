package nl.thijsalders.spigotproxy.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.minecraft.server.v1_10_R1.NetworkManager;
import nl.thijsalders.spigotproxy.haproxy.HAProxyMessage;
import nl.thijsalders.spigotproxy.haproxy.HAProxyMessageDecoder;

public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	
	private ChannelInitializer<SocketChannel> oldChildHandler;
	private Method oldChildHandlerMethod;
	
	public NettyChannelInitializer(ChannelInitializer<SocketChannel> oldChildHandler) throws Exception {
		this.oldChildHandler = oldChildHandler;
		this.oldChildHandlerMethod = this.oldChildHandler.getClass().getDeclaredMethod("initChannel", Channel.class);
		this.oldChildHandlerMethod.setAccessible(true);
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
                    
                    SocketAddress socketaddr = InetSocketAddress.createUnresolved(realaddress, realport);
                    
                    NetworkManager networkmanager = (NetworkManager) channel.pipeline().get("packet_handler");
            		networkmanager.l = socketaddr;
                } else {
                    super.channelRead(ctx, msg);
                }
            }
		});
	}

}