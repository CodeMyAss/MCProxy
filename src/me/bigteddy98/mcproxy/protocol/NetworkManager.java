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
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;

public class NetworkManager {

	private volatile ConnectionState currentState = ConnectionState.HANDSHAKE;
	private volatile int protocolId;

	public synchronized void handleServerBoundPacket(ByteBuf bufferClone) {
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		while (true) {
			if (bufferClone.readableBytes() == 0) {
				return;
			}
			wrapper.readVarInt();
			int id = wrapper.readVarInt();
			if (currentState == ConnectionState.HANDSHAKE && id == 0x00) {
				protocolId = wrapper.readVarInt();
				wrapper.readString();
				wrapper.readUnsignedShort();
				currentState = ConnectionState.fromId(wrapper.readVarInt());
				continue;
			} else {
				return;
			}
		}
		// ProxyLogger.debug("client --> server");
	}

	public synchronized void handleClientBoundPacket(ByteBuf bufferClone) {
		// ProxyLogger.debug("server --> client");
	}
}
