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

import me.bigteddy98.mcproxy.Main;
import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.api.Player;
import me.bigteddy98.mcproxy.api.entity.Achievement;
import me.bigteddy98.mcproxy.entity.Location;
import me.bigteddy98.mcproxy.protocol.codex.CompressionCodex;
import me.bigteddy98.mcproxy.protocol.codex.DecompressionCodex;
import me.bigteddy98.mcproxy.protocol.handlers.ClientSideHandler;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class NetworkManager implements Player {

	public final ClientSideHandler clientSideHandler;
	public volatile ConnectionState currentState = ConnectionState.HANDSHAKE;
	public volatile int protocolId;

	public Deflater deflater = new Deflater();
	public Inflater inflater = new Inflater();
	public int compressionThreshold = -1;
	public boolean clientsideCompressionEnabled = false;
	public boolean serversideCompressionEnabled = false;

	public ChannelPipeline serversidePipeline;
	public ChannelPipeline clientsidePipeline;

	public String playerName;
	public String uuid;

	public NetworkManager(ClientSideHandler clientToProxyHandler) {
		this.clientSideHandler = clientToProxyHandler;
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

	public void disconnect() {
		this.clientsidePipeline.close();
		this.serversidePipeline.close();
		Main.getInstance().getDataManager().disconnectPlayer(this);
	}

	public void enableServerSideCompression() {
		if (this.serversideCompressionEnabled) {
			return;
		}
		this.serversideCompressionEnabled = true;
		ProxyLogger.info("Serversided compression threshold is now set to " + this.compressionThreshold);
		this.serversidePipeline.addBefore("serverbound_proxy_codex", "decompression_codex", new DecompressionCodex(this));
		this.serversidePipeline.addBefore("serverbound_proxy_codex", "compression_codex", new CompressionCodex(this));
	}

	public void enableClientSideCompression() {
		if (this.clientsideCompressionEnabled) {
			return;
		}
		this.clientsideCompressionEnabled = true;
		ProxyLogger.info("Clientsided compression threshold is now set to " + this.compressionThreshold);
		this.clientsidePipeline.addBefore("clientbound_proxy_codex", "decompression_codex", new DecompressionCodex(this));
		this.clientsidePipeline.addBefore("clientbound_proxy_codex", "compression_codex", new CompressionCodex(this));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientSideHandler == null) ? 0 : clientSideHandler.hashCode());
		result = prime * result + (clientsideCompressionEnabled ? 1231 : 1237);
		result = prime * result + ((clientsidePipeline == null) ? 0 : clientsidePipeline.hashCode());
		result = prime * result + compressionThreshold;
		result = prime * result + ((currentState == null) ? 0 : currentState.hashCode());
		result = prime * result + (serversideCompressionEnabled ? 1231 : 1237);
		result = prime * result + ((serversidePipeline == null) ? 0 : serversidePipeline.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetworkManager other = (NetworkManager) obj;
		if (clientSideHandler == null) {
			if (other.clientSideHandler != null)
				return false;
		} else if (!clientSideHandler.equals(other.clientSideHandler))
			return false;
		if (clientsideCompressionEnabled != other.clientsideCompressionEnabled)
			return false;
		if (clientsidePipeline == null) {
			if (other.clientsidePipeline != null)
				return false;
		} else if (!clientsidePipeline.equals(other.clientsidePipeline))
			return false;
		if (compressionThreshold != other.compressionThreshold)
			return false;
		if (currentState != other.currentState)
			return false;
		if (serversideCompressionEnabled != other.serversideCompressionEnabled)
			return false;
		if (serversidePipeline == null) {
			if (other.serversidePipeline != null)
				return false;
		} else if (!serversidePipeline.equals(other.serversidePipeline))
			return false;
		return true;
	}

	@Override
	public String getName() {
		return this.playerName;
	}

	@Override
	public void awardAchievement(Achievement achievement) {
		Main.getInstance().executeCommand("achievement give " + achievement.getName() + " " + this.getName());
	}

	@Override
	public void sendMessage(String message) {
		// TODO
	}

	@Override
	public void teleport(Location loc) {
		Main.getInstance().executeCommand("tp " + this.getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
	}
}
