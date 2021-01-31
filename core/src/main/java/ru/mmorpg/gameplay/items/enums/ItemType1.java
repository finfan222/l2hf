package ru.mmorpg.gameplay.items.enums;

import lombok.Getter;
import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum ItemType1 implements Enumerate {
	TYPE1_WEAPON_RING_EARRING_NECKLACE(0),
	TYPE1_SHIELD_ARMOR(1),
	TYPE1_ITEM_QUEST_ITEM_ADENA(4);

	@Getter private final int id;

	ItemType1(int id) {
		this.id = id;
	}

	@Override
	public ItemType1 get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public ItemType1[] array() {
		return values();
	}

	@Override
	public String title() {
		return TextUtils.normalize(this);
	}

	@Override
	public int id() {
		return id;
	}

	@Override
	public int order() {
		return ordinal();
	}
}
