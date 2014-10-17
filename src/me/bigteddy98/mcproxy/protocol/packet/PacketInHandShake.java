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
package me.bigteddy98.mcproxy.protocol.packet;

import me.bigteddy98.mcproxy.protocol.ConnectionState;
import me.bigteddy98.mcproxy.protocol.NetworkManager;

public class PacketInHandShake extends Packet {

	private int protocolVersion;
	private String serverAddress;
	private int port;
	private int nextState;

	public PacketInHandShake() {}

	public PacketInHandShake(int protocolVersion, String serverAddress, int port, int nextState) {
		this.protocolVersion = protocolVersion;
		this.serverAddress = serverAddress;
		this.port = port;
		this.nextState = nextState;
	}

	@Override
	public void read(PacketDataWrapper wrapper) {
		this.protocolVersion = wrapper.readVarInt();
		this.serverAddress = wrapper.readString();
		this.port = wrapper.readShort();
		this.nextState = wrapper.readVarInt();
	}

	@Override
	public void write(PacketDataWrapper wrapper) {
		wrapper.writeVarInt(protocolVersion);
		wrapper.writeString(serverAddress);
		wrapper.writeShort(port);
		wrapper.writeInt(nextState);
	}

	@Override
	public void onReceive(NetworkManager networkManager, PacketReceiveEvent event) {
		networkManager.currentState = ConnectionState.fromId(this.nextState);
	}

	public int getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getNextState() {
		return nextState;
	}

	public void setNextState(int nextState) {
		this.nextState = nextState;
	}
}
