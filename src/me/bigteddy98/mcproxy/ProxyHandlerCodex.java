package me.bigteddy98.mcproxy;

/* 
 * MCProxy
 * Copyright (C) 2014 Sander Gielisse
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;

public class ProxyHandlerCodex extends ChannelHandlerAdapter {

	private final String hostname;
	private final int port;

	private volatile Channel incomingChannel;
	private volatile Channel outgoingChannel;

	public ProxyHandlerCodex(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		incomingChannel = ctx.channel();

		Bootstrap bootstrab = new Bootstrap();
		bootstrab.group(incomingChannel.eventLoop());
		bootstrab.channel(ctx.channel().getClass());
		bootstrab.handler(new ProxyForwardCodex(incomingChannel));
		bootstrab.option(ChannelOption.AUTO_READ, false);
		ChannelFuture f = bootstrab.connect(hostname, port);
		
		outgoingChannel = f.channel();
		f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					incomingChannel.read();
				} else {
					incomingChannel.close();
				}
			}
		});
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {		
		if (outgoingChannel.isActive()) {
			System.out.println("Sending to server " + msg.toString());
			outgoingChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						ctx.channel().read();
					} else {
						future.channel().close();
					}
				}
			});
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ProxyLogger.exception(cause);
		super.exceptionCaught(ctx, cause);
	}
}