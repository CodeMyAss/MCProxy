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
import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class NetworkManager {

	public final ProxyHandlerCodex proxyHandlerCodex;
	
	public volatile ConnectionState currentState = ConnectionState.HANDSHAKE;
	public volatile int protocolId;

	public NetworkManager(ProxyHandlerCodex proxyHandlerCodex) {
		this.proxyHandlerCodex = proxyHandlerCodex;
	}

	public synchronized ByteBuf handleServerBoundPacket(ByteBuf originalBuffer, ByteBuf bufferClone) throws InstantiationException, IllegalAccessException {
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		while (true) {
			if (bufferClone.readableBytes() == 0) {
				return originalBuffer;
			}
			wrapper.readVarInt();
			int id = wrapper.readVarInt();

			Packet packet = PacketRegistry.getServerBoundPacket(id, this.currentState).newInstance();
			ProxyLogger.debug("Handled " + packet);
			if (packet == null) {
				try {
					throw new RuntimeException("Unable to find serverbound packet with ID " + id + " and connectionState " + this.currentState);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return originalBuffer;
			}
			packet.read(wrapper);
			packet.onReceive(this, new PacketReceiveEvent());
		}
		// ProxyLogger.debug("client --> server");
	}

	public synchronized ByteBuf handleClientBoundPacket(ByteBuf originalBuffer, ByteBuf bufferClone) throws InstantiationException, IllegalAccessException {		
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		while (true) {
			if (bufferClone.readableBytes() == 0) {
				return originalBuffer;
			}
			wrapper.readVarInt();
			int id = wrapper.readVarInt();

			Packet packet = PacketRegistry.getClientBoundPacket(id, this.currentState).newInstance();
			ProxyLogger.debug("Handled " + packet);
			if (packet == null) {
				try {
					throw new RuntimeException("Unable to find clientbound packet with ID " + id + " and connectionState " + this.currentState);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return originalBuffer;
			}
			packet.read(wrapper);
			packet.onReceive(this, new PacketReceiveEvent());
		}
		// ProxyLogger.debug("server --> client");
	}
}
