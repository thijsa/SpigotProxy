package nl.thijsalders.spigotproxy.netty;

import io.netty.channel.ChannelHandlerContext;

public interface NettyInjectHandler {

	public void packetReceived(NettyDecoderHandler handler, ChannelHandlerContext context, Object object) throws Exception;

	public boolean isEnabled();
	
}
