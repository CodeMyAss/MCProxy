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
package me.bigteddy98.mcproxy.api.entity;

public enum Achievement {
	
	OPEN_INVENTORY("achievement.openInventory"),
	NETHER_PORTAL("achievement.portal"),
	SPAWN_WITHER("achievement.spawnWither"),
	KILL_WITHER("achievement.killWither"),
	GET_BLAZE_ROD("achievement.blazeRod"),
	MAKE_BREAD("achievement.makeBread"),
	BAKE_CAKE("achievement.bakeCake"),
	BUILD_BETTER_PICKAXE("achievement.buildBetterPickaxe"),
	COOK_FISH("achievement.cookFish"),
	BREED_COW("achievement.breedCow"),
	DIAMONDS_TO_YOU("achievement.diamondsToYou"),
	BREW_POTION("achievement.potion"),
	ON_A_RAIL("achievement.onARail"),
	BUILD_SWORD("achievement.buildSword"),
	KILL_ENEMY("achievement.killEnemy"),
	KILL_COW("achievement.killCow"),
	OVERKILL("achievement.overkill"),
	BOOKCASE("achievement.bookcase"),
	EXPLORE_ALL_BIOMES("achievement.exploreAllBiomes"),
	END_PORTAL("achievement.theEnd"),
	THE_END("achievement.theEnd2"),
	ENCHANTMENTS("achievement.enchantments"),
	BUILD_PICKAXE("achievement.buildPickaxe"),
	BUILD_FURNACE("achievement.buildFurnace"),
	FULL_BEACON("achievement.fullBeacon"),
	MINE_WOOD("achievement.mineWood"),
	BUILD_WORKBENCH("achievement.buildWorkBench"),
	FLY_PIG("achievement.flyPig"),
	SNIPE_SKELETON("achievement.snipeSkeleton"),
	GET_DIAMONDS("achievement.diamonds"),
	GHAST_RETURN("achievement.ghast"),
	ACQUIRE_IRON("achievement.acquireIron"),
	BUILD_HOE("achievement.buildHoe"),
	;
	
	private final String name;

	private Achievement(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
