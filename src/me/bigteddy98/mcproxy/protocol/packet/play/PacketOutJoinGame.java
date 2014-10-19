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
package me.bigteddy98.mcproxy.protocol.packet.play;

import me.bigteddy98.mcproxy.ProxyLogger;
import me.bigteddy98.mcproxy.protocol.NetworkManager;
import me.bigteddy98.mcproxy.protocol.packet.Packet;
import me.bigteddy98.mcproxy.protocol.packet.PacketDataWrapper;
import me.bigteddy98.mcproxy.protocol.packet.PacketReceiveEvent;

public class PacketOutJoinGame extends Packet {

	private int playerId;
	private int gamemode;
	private byte dimension;
	private int difficulty;
	private int maxPlayers;
	private String levelType;
	private boolean reducedDebugInfo;
	
	public PacketOutJoinGame() {}

	public PacketOutJoinGame(int playerId, int gamemode, byte dimension, byte difficulty, byte maxPlayers, String levelType, boolean reducedDebugInfo) {
		this.playerId = playerId;
		this.gamemode = gamemode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
		this.levelType = levelType;
		this.reducedDebugInfo = reducedDebugInfo;
	}

	@Override
	public void read(PacketDataWrapper wrapper) {
		this.playerId = wrapper.readInt();
		this.gamemode = wrapper.readUnsignedByte();
		this.dimension = wrapper.readByte();
		this.difficulty = wrapper.readUnsignedByte();
		this.maxPlayers = wrapper.readUnsignedByte();
		this.levelType = wrapper.readString();
		this.reducedDebugInfo = wrapper.readBoolean();
	}

	@Override
	public void write(PacketDataWrapper wrapper) {
		wrapper.writeInt(this.playerId);
		wrapper.writeByte(this.gamemode);
		wrapper.writeByte(this.dimension);
		wrapper.writeByte(this.difficulty);
		wrapper.writeByte(this.maxPlayers);
		wrapper.writeString(this.levelType);
		wrapper.writeBoolean(this.reducedDebugInfo);
	}

	@Override
	public void onReceive(NetworkManager networkManager, PacketReceiveEvent event) {
		// TODO
		ProxyLogger.debug("Player with ID " + this.playerId + " and gamemode " + this.gamemode + " with max players " + this.maxPlayers + " and levelType " + this.levelType);
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getGamemode() {
		return gamemode;
	}

	public void setGamemode(int gamemode) {
		this.gamemode = gamemode;
	}

	public byte getDimension() {
		return dimension;
	}

	public void setDimension(byte dimension) {
		this.dimension = dimension;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(byte difficulty) {
		this.difficulty = difficulty;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(byte maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

	public boolean isReducedDebugInfo() {
		return reducedDebugInfo;
	}

	public void setReducedDebugInfo(boolean reducedDebugInfo) {
		this.reducedDebugInfo = reducedDebugInfo;
	}
}
