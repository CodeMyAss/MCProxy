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
package me.bigteddy98.mcproxy.protocol.packet.login;

import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.NetworkManager;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class PacketOutCompression extends Packet {

	private int threshold;

	public PacketOutCompression() {}

	public PacketOutCompression(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void read(PacketDataWrapper wrapper) {
		this.threshold = wrapper.readVarInt();
	}

	@Override
	public void write(PacketDataWrapper wrapper) {
		wrapper.writeVarInt(threshold);
	}

	@Override
	public void onReceive(NetworkManager networkManager, PacketReceiveEvent event) {
		networkManager.compressionThreshold = this.threshold;
		ProxyLogger.info("Compression threshold is now set to " + this.threshold);
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
}
