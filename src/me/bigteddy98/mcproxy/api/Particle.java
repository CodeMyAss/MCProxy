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

public enum Particle {
	
	EXPLODE("explode"),
	LARGE_EXPLODE("largeexplode"),
	HUGE_EXPLOSION("hugeexplosion"),
	FIREWORK_SPARK("fireworksSpark"),
	BUBBLE("bubble"),
	SPLASH("splash"),
	WAKE("wake"),
	SUSPENDED("suspended"),
	DEPTH_SUSPENDED("depthsuspend"),
	CRIT("crit"),
	MAGIC_CRIT("magicCrit"),
	SMOKE("smoke"),
	LARGE_SMOKE("largesmoke"),
	SPELL("spell"),
	INSTANT_SPELL("instantSpell"),
	MOB_SPELL("mobSpell"),
	MOB_SPELL_AMBIENT("mobSpellAmbient"),
	WITCH_MAGIC("witchMagic"),
	DRIP_WATER("dripWater"),
	DRIP_LAVA("dripLava"),
	ANGRY_VILLAGER("angryVillager"),
	HAPPY_VILLAGER("happyVillager"),
	TOWN_AURA("townaura"),
	NOTE("note"),
	PORTAL("portal"),
	ENCHANTMENT_TABLE("enchantmenttable"),
	FLAME("flame"),
	LAVA("lava"),
	FOOTSTEP("footstep"),
	RED_DUST("reddust"),
	SNOWBALL_POOF("snowballpoof"),
	SLIME("slime"),
	HEART("heart"),
	BARRIER("barrier"),
	CLOUD("cloud"),
	SNOWSHOVEL("snowshovel"),
	;
	
	private final String name;

	private Particle(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
