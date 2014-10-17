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
package me.bigteddy98.mcproxy.entity;

public class Location implements Cloneable {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public Location(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Location(double x, double y, double z, double yaw, double pitch) {
		this((float) x, (float) y, (float) z, (float) yaw, (float) pitch);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public static int floor(double num) {
		final int floor = (int) num;
		return floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
	}

	public int getBlockX() {
		return floor(getX());
	}

	public int getBlockY() {
		return floor(getY());
	}

	public int getBlockZ() {
		return floor(getZ());
	}

	public Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public boolean isInRange(int viewDistance, Location otherLoc, boolean checkY) {
		if (!checkY) {
			return Math.max(this.x < otherLoc.x ? otherLoc.x - this.x : this.x - otherLoc.x, this.z < otherLoc.z ? otherLoc.z - this.z : this.z - otherLoc.z) < viewDistance;
		} else {
			return Math.max(this.x < otherLoc.x ? otherLoc.x - this.x : this.x - otherLoc.x, Math.max(this.z < otherLoc.z ? otherLoc.z - this.z : this.z - otherLoc.z, this.y < otherLoc.y ? otherLoc.y - this.y : this.y - otherLoc.y)) < viewDistance;
		}
	}

	public double distance(Location otherLocation) {
		double tempX = Math.abs(otherLocation.getX() - this.getX());
		double tempY = Math.abs(otherLocation.getY() - this.getY());
		double tempZ = Math.abs(otherLocation.getZ() - this.getZ());
		return Math.sqrt(tempX * tempX + tempY * tempY + tempZ * tempZ);
	}

	public double distanceSquared(Location otherLocation) {
		double tempX = Math.abs(otherLocation.getX() - this.getX());
		double tempY = Math.abs(otherLocation.getY() - this.getY());
		double tempZ = Math.abs(otherLocation.getZ() - this.getZ());
		return tempX * tempX + tempY * tempY + tempZ * tempZ;
	}

	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(pitch);
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(yaw);
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (Float.floatToIntBits(pitch) != Float.floatToIntBits(other.pitch))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Float.floatToIntBits(yaw) != Float.floatToIntBits(other.yaw))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	public Location cloneFrom(Location cloneFrom) {
		this.x = cloneFrom.x;
		this.y = cloneFrom.y;
		this.z = cloneFrom.z;
		this.yaw = cloneFrom.yaw;
		this.pitch = cloneFrom.pitch;
		return this;
	}

	public int getChunkX() {
		return (getBlockX() >> 4);
	}

	public int getChunkZ() {
		return (getBlockX() >> 4);
	}

}