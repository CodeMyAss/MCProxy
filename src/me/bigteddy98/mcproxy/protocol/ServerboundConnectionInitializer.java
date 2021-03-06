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

import me.bigteddy98.mcproxy.protocol.handlers.ServerSideHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerboundConnectionInitializer extends ChannelInitializer<SocketChannel> {

	private final NetworkManager networkManager;
	private final Channel inboundChannel;

	public ServerboundConnectionInitializer(NetworkManager networkManager, Channel inboundChannel) {
		this.networkManager = networkManager;
		this.inboundChannel = inboundChannel;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("serverbound_proxy_codex", new ServerSideHandler(networkManager, inboundChannel));
	}
}
