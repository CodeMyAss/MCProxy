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
package me.bigteddy98.mcproxy;

import java.util.ArrayList;
import java.util.List;

import me.bigteddy98.mcproxy.protocol.NetworkManager;

public class DataManager {

	private final List<NetworkManager> onlinePlayers = new ArrayList<>();

	public void joinPlayer(NetworkManager networkManager) {
		synchronized (onlinePlayers) {
			onlinePlayers.add(networkManager);
			ProxyLogger.debug("PLAYER SIZE " + this.onlinePlayers.size());
		}
	}

	public void disconnectPlayer(NetworkManager networkManager) {
		synchronized (onlinePlayers) {
			if (onlinePlayers.contains(networkManager)) {
				onlinePlayers.remove(networkManager);
			}
			ProxyLogger.debug("PLAYER SIZE " + this.onlinePlayers.size());
		}
	}
}
