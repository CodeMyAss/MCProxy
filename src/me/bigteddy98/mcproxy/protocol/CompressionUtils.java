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

import java.util.zip.DataFormatException;

import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.codex.PacketUtils;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;

public class CompressionUtils {
	public static ByteBuf decompress(NetworkManager networkManager, ByteBuf buf) throws DataFormatException {
		PacketDataWrapper compressedBuffer = new PacketDataWrapper(buf);

		if (compressedBuffer.readableBytes() == 0) {
			return buf;
		}
		
		int totalSize = compressedBuffer.readVarInt(); // total byte count
		int uncompressedSize = compressedBuffer.readVarInt(); // uncompressed size
		int dataSize = totalSize - PacketUtils.getVarIntSize(uncompressedSize);
		
		ProxyLogger.debug("Decompressing... totalSize: " + totalSize + " uncompressedSize: " + uncompressedSize + " dataSize: " + dataSize);

		ByteBuf tempBuf = Unpooled.buffer();
		PacketDataWrapper uncompressedBuffer = new PacketDataWrapper(tempBuf);

		if (uncompressedSize == 0) {
			uncompressedBuffer.writeVarInt(0);
			uncompressedBuffer.writeBytes(compressedBuffer.getBuffer(), 0, dataSize);
		} else {
			ProxyLogger.debug("Size: " + buf.readableBytes());
			byte[] compressedPacket = new byte[buf.readableBytes()];
			compressedBuffer.readBytes(compressedPacket);

			networkManager.inflater.setInput(compressedPacket);
			byte[] uncompressedPacket = new byte[uncompressedSize];
			networkManager.inflater.inflate(uncompressedPacket);
			uncompressedBuffer.writeVarInt(uncompressedSize);
			uncompressedBuffer.writeBytes(uncompressedPacket);
			networkManager.inflater.reset();
		}
		return uncompressedBuffer.getBuffer();
	}
}
