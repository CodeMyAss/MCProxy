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
import me.bigteddy98.mcproxy.protocol.ConnectionState;
import me.bigteddy98.mcproxy.protocol.NetworkManager;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class PacketOutLoginSucces extends Packet {

	private String uuid;
	private String username;

	public PacketOutLoginSucces() {
	}

	public PacketOutLoginSucces(String uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}

	@Override
	public void read(PacketDataWrapper wrapper) {
		this.uuid = wrapper.readString();
		this.username = wrapper.readString();
		ProxyLogger.debug("Player " + this.username + " UUID " + this.uuid.toString());
	}

	@Override
	public void write(PacketDataWrapper wrapper) {
		wrapper.writeString(uuid);
		wrapper.writeString(username);
	}

	@Override
	public void onReceive(NetworkManager networkManager, PacketReceiveEvent event) {
		networkManager.currentState = ConnectionState.PLAY;
	}

	public String getUUID() {
		return uuid;
	}

	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
