package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum DamageType implements Enumerate {
	NONE,
	CUT,
	PIERCE,
	CRUSH,
	SLASH,
	BITING,
	PURE,
	MAGIC,
	ELEMENT,
	CLAW,
	FANG;

	@Override
	public DamageType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public DamageType[] array() {
		return values();
	}

	@Override
	public String title() {
		return TextUtils.normalize(this);
	}

	@Override
	public int id() {
		return order() + 1;
	}

	@Override
	public int order() {
		return ordinal();
	}
}
