package ru.mmorpg.gameplay.items;

import lombok.Getter;
import lombok.Setter;
import org.lineage.gameserver.enums.ItemLocation;
import ru.mmorpg.gameplay.AbstractEntity;
import ru.mmorpg.gameplay.items.enums.*;
import ru.mmorpg.structure.Num;

import java.util.Objects;

/**
 * @author finfan
 */
@Getter
@Setter
public class Item extends AbstractEntity {

	private final ItemData data;
	private ItemLocation itemLocation;
	private ItemManipulation lastManipulation = ItemManipulation.UNCHANGED;
	private Num count;
	private Num totalWeight;
	private Num duration;
	private Num durability;
	private Num enchant;
	private Num augment;
	private boolean isBlocked;

	public Item(int uuid, ItemData data, ItemLocation target, long count) {
		super(uuid);
		this.data = data;
		this.itemLocation = target;
		this.count = data.isStackable() ? new Num(0, count) : new Num(0, 1);
		this.totalWeight = new Num(0,count * data.getWeight());
		if(data.getDurability() > 0) {
			this.durability = new Num(0, data.getDurability());
		}
		if(data.getDuration() > 0) {
			this.duration = new Num(0, data.getDuration());
		}
		this.enchant = new Num(0, 0);
		this.augment = new Num(0,0);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Item)) return false;
		if (!super.equals(o)) return false;
		Item item1 = (Item) o;
		return Objects.equals(uuid, item1.getUuid());
	}

	@Override
	public int hashCode() {
		return uuid;
	}

	public int getId() {
		return data.getId();
	}

	public ItemType1 getType1() {
		return data.getItemType1();
	}

	public ItemType2 getType2() {
		return data.getItemType2();
	}

	public void increase(long value) {
		if(value > Long.MAX_VALUE) {
			value = Long.MAX_VALUE;
		}

		count.add(value);
		totalWeight.add(value);
		lastManipulation = ItemManipulation.MODIFY;
	}

	/**
	 * Уменьшаеит кол-во вещей на {@link Long} {@code value}.
	 * <ul>
	 *     <li>снижает count</li>
	 *     <li>снижает totalWeight всех итемов</li>
	 *     <li>устанавливает последнюю манипуляцию {@link ItemManipulation#MODIFY}
	 *     если вещи остались и {@link ItemManipulation#REMOVE} если вещь должна удалиться</li>
	 * </ul>
	 *
	 * @param value
	 */
	public ItemManipulation decrease(long value) {
		if(value > count.longValue()) {
			value = count.longValue();
		}

		count.sub(value);
		totalWeight.sub(value * data.getWeight());
		if(count.longValue() == 0) {
			lastManipulation = ItemManipulation.REMOVE;
		} else {
			lastManipulation = ItemManipulation.MODIFY;
		}
		return lastManipulation;
	}

	public boolean isArmor() {
		return data.getGroupType() instanceof ArmorType;
	}

	public boolean isWeapon() {
		return data.getGroupType() instanceof WeaponType;
	}

	public boolean isJewel() {
		return data.getSlotType().isJewel();
	}

	public boolean isEtc() {
		return data.getGroupType() instanceof EtcType;
	}

	public boolean isArrow() {
		return data.getGroupType() == EtcType.ARROW;
	}

	public boolean isBolt() {
		return data.getGroupType() == EtcType.BOLT;
	}

	public boolean isEquipped() {
		return itemLocation == ItemLocation.PAPERDOLL;
	}

	public boolean isEquippable() {
		return isArmor() || isWeapon() || isArrow() || isBolt() || isJewel();
	}

	public PartType getPartType() {
		return data.getPartType();
	}

	public boolean isAugmented() {
		return augment.isZero();
	}
}
