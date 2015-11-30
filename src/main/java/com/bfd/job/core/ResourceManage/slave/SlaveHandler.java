package com.bfd.job.core.ResourceManage.slave;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * @author: BFD474
 *
 * @description: Slave端相当于服务器端
 */
public class SlaveHandler extends SimpleChannelUpstreamHandler {
	
	private static final Logger logger = Logger.getLogger(SlaveHandler.class);

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		// 接受消息处理
		logger.info("服务器接受到1：" + e.getMessage());
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		e.getChannel().write("Reply----");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		Channel ch = e.getChannel();
		ch.close();
	}

}
