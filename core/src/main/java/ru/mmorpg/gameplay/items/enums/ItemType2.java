package ru.mmorpg.gameplay.items.enums;

import lombok.Getter;
import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum ItemType2 implements Enumerate {
	TYPE2_WEAPON (0),
	TYPE2_SHIELD_ARMOR(1),
	TYPE2_ACCESSORY(2),
	TYPE2_QUEST(3),
	TYPE2_MONEY(4),
	TYPE2_OTHER(5);

	@Getter private final int id;

	ItemType2(int id) {
		this.id = id;
	}

	@Override
	public ItemType2 get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public ItemType2[] array() {
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
