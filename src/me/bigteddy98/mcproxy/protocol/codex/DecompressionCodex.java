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
package me.bigteddy98.mcproxy.protocol.codex;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import me.bigteddy98.mcproxy.protocol.BufferUtils;
import me.bigteddy98.mcproxy.protocol.NetworkManager;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;

public class DecompressionCodex extends ReplayingDecoder<Void> {

	private final NetworkManager networkManager;

	public DecompressionCodex(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
		PacketDataWrapper compressedBuffer = new PacketDataWrapper(buf);
		if (compressedBuffer.readableBytes() == 0) {
			return;
		}
		int totalSize = compressedBuffer.readVarInt(); // total byte count
		int uncompressedSize = compressedBuffer.readVarInt(); // uncompressed
																// size
		int dataSize = totalSize - PacketUtils.getVarIntSize(uncompressedSize);

		PacketDataWrapper uncompressedBuffer = new PacketDataWrapper(Unpooled.buffer());
		if (uncompressedSize == 0) {
			uncompressedBuffer.writeVarInt(dataSize);
			byte[] array = new byte[dataSize];
			buf.readBytes(array, 0, dataSize);
			uncompressedBuffer.writeBytes(array, 0, dataSize);
		} else {
			byte[] compressedPacket = new byte[dataSize];
			compressedBuffer.readBytes(compressedPacket);

			networkManager.inflater.setInput(compressedPacket);
			byte[] uncompressedPacket = new byte[uncompressedSize];
			networkManager.inflater.inflate(uncompressedPacket);
			uncompressedBuffer.writeVarInt(uncompressedSize);
			uncompressedBuffer.writeBytes(uncompressedPacket);
			networkManager.inflater.reset();
		}
		out.add(uncompressedBuffer.getBuffer());
	}
}