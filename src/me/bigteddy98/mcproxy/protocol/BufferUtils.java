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

public class BufferUtils {

	public static void printBufferHex(String name, ByteBuf buf) {
		buf.markReaderIndex();
		byte[] array = new byte[buf.readableBytes()];
		buf.readBytes(array, 0, buf.readableBytes());
		ProxyLogger.debug("Buffer hex data for name " + name + " " + getHexString(array));
		buf.resetReaderIndex();
	}

	private static final char[] hex = "0123456789ABCDEF".toCharArray();
	public static String getHexString(byte[] hexArray) {
		char[] hexChars = new char[hexArray.length * 3];
		int v;
		for (int j = 0; j < hexArray.length; j++) {
			v = hexArray[j] & 0xFF;
			hexChars[j * 3] = hex[v >>> 4];
			hexChars[j * 3 + 1] = hex[v & 0x0F];
			hexChars[j * 3 + 2] = ' ';
		}
		return new String(hexChars);
	}
}
