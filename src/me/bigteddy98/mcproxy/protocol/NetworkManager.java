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
import io.netty.channel.ChannelPipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.codex.CompressionCodex;
import me.bigteddy98.mcproxy.protocol.codex.DecompressionCodex;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class NetworkManager {

	public final ClientToProxyHandler clientToProxyHandler;

	public volatile ConnectionState currentState = ConnectionState.HANDSHAKE;
	public volatile int protocolId;

	public Deflater deflater = new Deflater();
	public Inflater inflater = new Inflater();
	public int compressionThreshold = -1;
	public boolean clientsideCompressionEnabled = false;
	public boolean serversideCompressionEnabled = false;

	public ChannelPipeline serversideHandler;
	public ChannelPipeline clientsideHandler;

	public NetworkManager(ClientToProxyHandler clientToProxyHandler) {
		this.clientToProxyHandler = clientToProxyHandler;
	}

	public synchronized List<Packet> handleServerBoundPackets(ByteBuf originalBuffer, ByteBuf bufferClone) throws InstantiationException, IllegalAccessException {
		List<Packet> list = new ArrayList<>();
		if (bufferClone.readableBytes() == 0) {
			return list;
		}
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		while (bufferClone.readableBytes() > 0) {
			bufferClone.markReaderIndex();
			int readBytes = bufferClone.readableBytes();
			int length = 0;
			{
				int bytes = 0;
				byte in;
				while (true) {
					if (readBytes < 1) {
						bufferClone.resetReaderIndex();
						return list;
					}
					in = bufferClone.readByte();
					length |= (in & 0x7F) << (bytes++ * 7);
					if (bytes > 5) {
						throw new RuntimeException("VarInt too big");
					}
					if ((in & 0x80) != 0x80) {
						break;
					}
				}
			}
			if (bufferClone.readableBytes() < length) {
				bufferClone.resetReaderIndex();
				return list;
			}
			int id = wrapper.readVarInt();
			Class<? extends Packet> clazz = PacketRegistry.getServerBoundPacket(id, this.currentState);
			if (clazz == null) {
				ProxyLogger.warn("Unknown packet ID 0x" + Integer.toHexString(id) + " and state " + this.currentState);
				return list;
			}
			Packet packet = clazz.newInstance();
			packet.read(wrapper);
			packet.onReceive(this, new PacketReceiveEvent());
			list.add(packet);
			ProxyLogger.debug("Handled " + packet.toString());
			bufferClone.discardSomeReadBytes();
		}
		return list;
	}

	public synchronized List<Packet> handleClientBoundPackets(ByteBuf originalBuffer, ByteBuf bufferClone) throws InstantiationException, IllegalAccessException {
		List<Packet> list = new ArrayList<>();
		if (bufferClone.readableBytes() == 0) {
			return list;
		}
		PacketDataWrapper wrapper = new PacketDataWrapper(bufferClone);
		while (bufferClone.readableBytes() > 0) {
			bufferClone.markReaderIndex();
			int readBytes = bufferClone.readableBytes();
			int length = 0;
			{
				int bytes = 0;
				byte in;
				while (true) {
					if (readBytes < 1) {
						bufferClone.resetReaderIndex();
						return list;
					}
					in = bufferClone.readByte();
					length |= (in & 0x7F) << (bytes++ * 7);
					if (bytes > 5) {
						throw new RuntimeException("VarInt too big");
					}
					if ((in & 0x80) != 0x80) {
						break;
					}
				}
			}
			if (bufferClone.readableBytes() < length) {
				bufferClone.resetReaderIndex();
				return list;
			}
			int id = wrapper.readVarInt();
			Class<? extends Packet> clazz = PacketRegistry.getClientBoundPacket(id, this.currentState);
			if (clazz == null) {
				ProxyLogger.warn("Unknown packet ID 0x" + Integer.toHexString(id) + " and state " + this.currentState);
				return list;
			}
			Packet packet = clazz.newInstance();
			packet.read(wrapper);
			packet.onReceive(this, new PacketReceiveEvent());
			list.add(packet);
			ProxyLogger.debug("Handled " + packet.toString());
			bufferClone.discardSomeReadBytes();
		}
		return list;
	}

	public void enableServerSideCompression() {
		if(this.serversideCompressionEnabled){
			return;
		}
		this.serversideCompressionEnabled = true;
		ProxyLogger.info("Serversided compression threshold is now set to " + this.compressionThreshold);
		this.serversideHandler.addBefore("serverbound_proxy_codex", "decompression_codex", new DecompressionCodex(this));
		this.serversideHandler.addBefore("serverbound_proxy_codex", "compression_codex", new CompressionCodex(this));
	}
	
	public void enableClientSideCompression(){
		if(this.clientsideCompressionEnabled){
			return;
		}
		this.clientsideCompressionEnabled = true;
		ProxyLogger.info("Clientsided compression threshold is now set to " + this.compressionThreshold);
		this.clientsideHandler.addBefore("clientbound_proxy_codex", "decompression_codex", new DecompressionCodex(this));
		this.clientsideHandler.addBefore("clientbound_proxy_codex", "compression_codex", new CompressionCodex(this));
	}
}
