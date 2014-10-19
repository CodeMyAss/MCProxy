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

import me.bigteddy98.mcproxy.api.entity.Achievement;
import me.bigteddy98.mcproxy.entity.Location;
import me.bigteddy98.mcproxy.inventory.ItemStack;

public interface Player {
	
	public String getName();
	
	public void awardAchievement(Achievement achievement);
	
	public void sendMessage(String message);
	
	public void teleport(Location location);
	
	public void clearInventory();
	
	public void addPotionEffect(PotionEffect effect, int seconds, int amplifier, boolean hideParticles);
	
	public void removePotionEffects();
	
	public void setGameMode(GameMode gameMode);
	
	public void giveItem(ItemStack stack);
	
	public void kick(String reason);
	
	public void playSound(String soundname);
	
	public void playSound(String soundname, float x, float y, float z);
	
	public void setInventoryItem(InventoryType type, int slotNumber, ItemStack stack);
	
	public void displayTitle(String message);

	public void displaySubTitle(String message);
}

