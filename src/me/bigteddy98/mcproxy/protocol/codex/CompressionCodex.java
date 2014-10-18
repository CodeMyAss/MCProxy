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
import io.netty.handler.codec.MessageToByteEncoder;
import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.NetworkManager;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;

public class CompressionCodex extends MessageToByteEncoder<ByteBuf> {

	private final NetworkManager networkManager;

	public CompressionCodex(NetworkManager networkManager) {
		this.networkManager = networkManager;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf bufIn, ByteBuf outgoingCompressedBuffer) throws Exception {
		ByteBuf bufOut = Unpooled.buffer();
		PacketDataWrapper incoming = new PacketDataWrapper(bufIn);
		PacketDataWrapper outgoing = new PacketDataWrapper(bufOut);
		
		//format:
		// packet length VarInt
		// packet data
		
		int totalSize = incoming.readVarInt();

		if (totalSize < networkManager.compressionThreshold) {
			outgoing.writeVarInt(0);
			
			byte[] array = new byte[totalSize];
			bufIn.readBytes(array, 0, totalSize);
			bufOut.writeBytes(array, 0, totalSize);
		} else {
			ByteBuf temporarilyBuf = Unpooled.buffer();
			try {

				byte[] array = new byte[totalSize];
				incoming.readBytes(array); // the whole uncompressed packet

				networkManager.deflater.setInput(array);
				networkManager.deflater.finish();

				byte[] buffer = new byte[1 * 1024];
				do {
					int size = networkManager.deflater.deflate(buffer);
					temporarilyBuf.writeBytes(buffer, 0, size);
				} while (!networkManager.deflater.finished());
				networkManager.deflater.reset();

				outgoing.writeVarInt(totalSize);
				outgoing.writeBytes(temporarilyBuf);
			} finally {
				temporarilyBuf.release();
			}
		}
		PacketDataWrapper wrapper = new PacketDataWrapper(outgoingCompressedBuffer);
		wrapper.writeVarInt(bufOut.readableBytes());
		wrapper.writeBytes(bufOut);
	}
}