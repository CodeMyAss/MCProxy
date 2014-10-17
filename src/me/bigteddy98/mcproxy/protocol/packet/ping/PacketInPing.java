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
package me.bigteddy98.mcproxy.protocol.packet.ping;

import me.bigteddy98.mcproxy.protocol.NetworkManager;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class PacketInPing extends Packet {

	private long time;

	public PacketInPing() {}

	public PacketInPing(long time) {
		this.time = time;
	}

	@Override
	public void read(PacketDataWrapper wrapper) {
		this.time = wrapper.readLong();
	}

	@Override
	public void write(PacketDataWrapper wrapper) {
		wrapper.writeLong(time);
	}

	@Override
	public void onReceive(NetworkManager networkManager, PacketReceiveEvent event) {
		// TODO
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
