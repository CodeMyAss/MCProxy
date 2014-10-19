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
package me.bigteddy98.mcproxy.api;

import java.util.Collection;

import me.bigteddy98.mcproxy.Main;

public class Server {

	public Collection<? extends Player> getOnlinePlayers() {
		return Main.getInstance().getDataManager().getClonedPlayerList();
	}
	
	public void banPlayer(Player player, String reason){
		this.banPlayer(player.getName(), reason);
	}
	
	public void banPlayer(String name, String reason){
		Main.getInstance().executeCommand("ban " + name + " " + reason);
	}
	
	public void banIP(String ip, String reason){
		Main.getInstance().executeCommand("ban-ip " + ip + " " + reason);
	}
	
	@Deprecated
	public void setDefaultGameMode(int gameModeId){
		Main.getInstance().executeCommand("defaultgamemode " + gameModeId);
	}
	
	public void setDefaultGameMode(GameMode gameMode){
		Main.getInstance().executeCommand("defaultgamemode " + gameMode.getId());
	}
	
	public void addOperator(Player player){
		this.addOperator(player.getName());
	}
	
	public void addOperator(String name){
		Main.getInstance().executeCommand("op " + name);
	}
	
	public void removeOperator(Player player){
		this.removeOperator(player.getName());
	}
	
	public void removeOperator(String name){
		Main.getInstance().executeCommand("deop " + name);
	}
	
	public void setDifficulty(Difficulty difficulty){
		this.setDifficulty(difficulty.getId());
	}
	
	@Deprecated
	public void setDifficulty(int id){
		Main.getInstance().executeCommand("difficulty " + id);
	}
	
	public void setGameRule(String gameRule, String value){
		Main.getInstance().executeCommand("gamerule " + gameRule + " " + value);
	}
	
	public void kickPlayer(Player player, String reason){
		Main.getInstance().executeCommand("kick " + player.getName() + " " + reason);
	}
	
	public void kill(Player player){
		Main.getInstance().executeCommand("kill " + player.getName());
	}
	
	public void unban(String name){
		Main.getInstance().executeCommand("pardon " + name);
	}
	
	public void unbanIP(String ip){
		Main.getInstance().executeCommand("pardon-ip " + ip);
	}
	
	public void displayParticle(Particle particle, float x, float y, float z, float xOffset, float yOffset, float zOffset, int speed, int count){
		//TODO flush this straight into the players network stream
		Main.getInstance().executeCommand("particle " + particle.getName() + " " + x + " " + y + " " + z + " " + xOffset + " " + yOffset + " " + zOffset + " " + speed + " " + count);
	}
	
	public void saveAll(){
		Main.getInstance().executeCommand("save-all");
	}
	
	public void disableAutoSave(){
		Main.getInstance().executeCommand("save-off");
	}
	
	public void enableAutoSave(){
		Main.getInstance().executeCommand("save-on");
	}
}
