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

public enum PotionEffect {
	SPEED(1),
	SLOWNESS(2),
	HASTE(3),
	MINING_FATIGUE(4),
	STRENGTH(5),
	INSTANT_HEALTH(6),
	INSTANT_DAMAGE(7),
	JUMP_BOOST(8),
	NAUSEA(9),
	REGENERATION(10),
	RESISTANCE(11),
	FIRE_RESISTANCE(12),
	WATER_BREATHING(13),
	INVISIBILITY(14),
	BLINDNESS(15),
	NIGHT_VISION(16),
	HUNGER(17),
	WEAKNESS(18),
	POISON(19),
	WITHER(20),
	HEALTH_BOOTS(21),
	ABSORPTION(22),
	SATURATION(23)
	;

	private final int id;

	private PotionEffect(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
