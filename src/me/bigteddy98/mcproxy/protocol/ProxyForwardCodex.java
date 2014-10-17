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
package me.bigteddy98.mcproxy.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import me.bigteddy98.mcproxy.ProxyLogger;

public class ProxyForwardCodex extends ChannelHandlerAdapter {

	private final Channel inboundChannel;
	private final ProxyHandlerCodex proxyHandlerCodex;

	public ProxyForwardCodex(ProxyHandlerCodex proxyHandlerCodex, Channel inboundChannel) {
		this.proxyHandlerCodex = proxyHandlerCodex;
		this.inboundChannel = inboundChannel;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.read();
		ctx.write(Unpooled.EMPTY_BUFFER);
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf bufferOriginal = (ByteBuf) msg;
		try {
			ByteBuf bufferClone = Unpooled.copiedBuffer(bufferOriginal);
			msg = this.proxyHandlerCodex.networkManager.handleClientBoundPacket((ByteBuf) msg, bufferClone);
			bufferClone.release();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ChannelFuture handler = inboundChannel.writeAndFlush(msg);
		handler.addListener(new ChannelFutureListener() {
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

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ProxyLogger.exception(cause);
		super.exceptionCaught(ctx, cause);
	}
}
