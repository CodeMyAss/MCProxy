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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketInHandShake;
import me.bigteddy98.mcproxy.protocol.packet.ping.PacketInPing;
import me.bigteddy98.mcproxy.protocol.packet.ping.PacketInRequest;
import me.bigteddy98.mcproxy.protocol.packet.ping.PacketOutPing;
import me.bigteddy98.mcproxy.protocol.packet.ping.PacketOutResponse;

public class PacketRegistry {

	private static final Class<? extends Packet> HANDSHAKE_IN = PacketInHandShake.class;
	private static Map<Integer, Class<? extends Packet>> STATUS_IN;
	static {
		Map<Integer, Class<? extends Packet>> map = new HashMap<>();
		map.put(0x00, PacketInRequest.class);
		map.put(0x01, PacketInPing.class);
		STATUS_IN = Collections.unmodifiableMap(map);
	}

	private static Map<Integer, Class<? extends Packet>> STATUS_OUT;
	static {
		Map<Integer, Class<? extends Packet>> map = new HashMap<>();
		map.put(0x00, PacketOutResponse.class);
		map.put(0x01, PacketOutPing.class);
		STATUS_OUT = Collections.unmodifiableMap(map);
	}

	public static Class<? extends Packet> getClientBoundPacket(int id, ConnectionState state) {
		if (state == ConnectionState.STATUS) {
			return STATUS_OUT.get(id);
		}
		throw new RuntimeException("Unknown packet ID " + id + " with connectionState " + state);
	}

	public static Class<? extends Packet> getServerBoundPacket(int id, ConnectionState state) {
		if (state == ConnectionState.HANDSHAKE) {
			if (id == 0x00) {
				return HANDSHAKE_IN;
			}
		} else if (state == ConnectionState.STATUS) {
			return STATUS_IN.get(id);
		}
		throw new RuntimeException("Unknown packet ID " + id + " with connectionState " + state);
	}
}
