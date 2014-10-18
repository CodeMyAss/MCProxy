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

import java.math.BigInteger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;
import me.bigteddy98.mcproxy.protocol.packet.login.PacketOutCompression;

public class NetworkManager {

	public final ProxyHandlerCodex proxyHandlerCodex;

	public volatile ConnectionState currentState = ConnectionState.HANDSHAKE;
	public volatile int protocolId;

	public Deflater deflater = new Deflater();
	public Inflater inflater = new Inflater();
	public int compressionThreshold = -1;

	public NetworkManager(ProxyHandlerCodex proxyHandlerCodex) {
		this.proxyHandlerCodex = proxyHandlerCodex;
	}

	public synchronized ByteBuf handleServerBoundPacket(ByteBuf originalBuffer, ByteBuf bufferClone) throws InstantiationException, IllegalAccessException {
		if (bufferClone.readableBytes() == 0) {
			return originalBuffer;
		}
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		if (this.compressionThreshold != -1) {
			try {
				wrapper = new PacketDataWrapper(CompressionUtils.decompress(this, bufferClone));
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
		}
		wrapper.readVarInt();
		boolean secondPacket = false;
		while (bufferClone.readableBytes() != 0) {
			if (secondPacket) {
				if (this.compressionThreshold == -1) {
					wrapper.readVarInt();
				}
			}
			int id = wrapper.readVarInt();
			Class<? extends Packet> clazz = PacketRegistry.getServerBoundPacket(id, this.currentState);
			if (clazz == null) {
				ProxyLogger.warn("Unknown serverBound packet with ID 0x" + Integer.toHexString(id) + " and state " + this.currentState);
				return originalBuffer;
			}
			Packet packet = clazz.newInstance();
			ProxyLogger.debug("Handled " + packet);
			packet.read(wrapper);
			packet.onReceive(this, new PacketReceiveEvent());
			if (packet instanceof PacketOutCompression) {
				return originalBuffer;
			}
			secondPacket = true;
		}
		return originalBuffer;
	}

	public synchronized ByteBuf handleClientBoundPacket(ByteBuf originalBuffer, ByteBuf bufferClone) throws InstantiationException, IllegalAccessException {
		if (bufferClone.readableBytes() == 0) {
			return originalBuffer;
		}
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		if (this.compressionThreshold != -1) {
			try {
				wrapper = new PacketDataWrapper(CompressionUtils.decompress(this, bufferClone));
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
		}
		wrapper.readVarInt();
		boolean secondPacket = false;
		while (bufferClone.readableBytes() != 0) {
			if (secondPacket) {
				if (this.compressionThreshold == -1) {
					wrapper.readVarInt();
				}
			}
			int id = wrapper.readVarInt();
			Class<? extends Packet> clazz = PacketRegistry.getClientBoundPacket(id, this.currentState);
			if (clazz == null) {
				ProxyLogger.warn("Unknown serverBound packet with ID 0x" + Integer.toHexString(id) + " and state " + this.currentState);
				return originalBuffer;
			}
			Packet packet = clazz.newInstance();
			ProxyLogger.debug("Handled " + packet);
			packet.read(wrapper);
			packet.onReceive(this, new PacketReceiveEvent());
			if (packet instanceof PacketOutCompression) {
				return originalBuffer;
			}
			secondPacket = true;
		}
		return originalBuffer;
	}
}
