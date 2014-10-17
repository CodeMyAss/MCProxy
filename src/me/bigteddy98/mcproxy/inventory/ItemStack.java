package me.bigteddy98.mcproxy.inventory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.jnbt.CompoundTag;
import org.jnbt.NBTInputStream;
import org.jnbt.NBTOutputStream;

public class ItemStack implements Cloneable {

	private short blockId;
	private byte amount;
	private short damage;
	private CompoundTag compoundTag;

	public ItemStack(short blockId, byte amount, short damage) {
		this.blockId = blockId;
		this.amount = amount;
		this.damage = damage;
	}

	public ItemStack(short blockId, byte amount, short damage, boolean generateNBTIfNotExist, CompoundTag compoundTag) {
		this.blockId = blockId;
		this.amount = amount;
		this.damage = damage;
		this.compoundTag = compoundTag;
	}

	public short getBlockId() {
		return blockId;
	}

	public void setBlockId(short blockId) {
		this.blockId = blockId;
	}

	public byte getAmount() {
		return amount;
	}

	public void setAmount(byte amount) {
		this.amount = amount;
	}

	public void setAmount(int amount) {
		if (amount > 256 || amount < 0)
			throw new IllegalArgumentException("Provided itemstack amount is to high! " + amount);
		this.amount = (byte) amount;
	}

	public short getDamage() {
		return damage;
	}

	public void setDamage(short damage) {
		this.damage = damage;
	}

	public CompoundTag getCompoundTag() {
		return compoundTag;
	}

	public void setCompoundTag(CompoundTag compoundTag) {
		this.compoundTag = compoundTag;
	}

	@Override
	public ItemStack clone() {
		try {
			ItemStack s = (ItemStack) super.clone();
			if (compoundTag == null)
				return s;
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			try (NBTOutputStream stream = new NBTOutputStream(bytes)) {
				stream.writeTag(compoundTag);
			}
			try (NBTInputStream stream = new NBTInputStream(new ByteArrayInputStream(bytes.toByteArray()))) {
				s.setCompoundTag((CompoundTag) stream.readTag());
			}
			return s;
		} catch (CloneNotSupportedException | IOException err) {
			throw new AssertionError(err);
		}
	}

	public ItemStack zeroSizeClone() {
		ItemStack i = this.clone();
		i.setAmount((byte) 0);
		return i;
	}

	public boolean materialTypeMatches(ItemStack other) {
		return other.blockId == this.blockId && other.damage == this.damage && (other.compoundTag == null ? this.compoundTag == null : other.compoundTag.equals(this.compoundTag));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + blockId;
		result = prime * result + ((compoundTag == null) ? 0 : compoundTag.hashCode());
		result = prime * result + damage;
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
		ItemStack other = (ItemStack) obj;
		if (amount != other.amount)
			return false;
		if (blockId != other.blockId)
			return false;
		if (compoundTag == null) {
			if (other.compoundTag != null)
				return false;
		} else if (!compoundTag.equals(other.compoundTag))
			return false;
		if (damage != other.damage)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ItemStack [blockId=" + blockId + ", amount=" + amount + ", damage=" + damage + ", compoundTag=" + compoundTag + "]";
	}
}