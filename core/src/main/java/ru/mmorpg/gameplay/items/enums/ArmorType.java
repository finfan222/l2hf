package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum ArmorType implements Enumerate {

	HEAVY,
	MEDIUM,
	LIGHT,
	MAGIC, // L2
	SIGILL, // L2
	SHIELD;

	@Override
	public ArmorType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public ArmorType[] array() {
		return values();
	}

	@Override
	public String title() {
		return TextUtils.normalize(this);
	}

	@Override
	public int id() {
		return ordinal() + 1;
	}

	@Override
	public int order() {
		return ordinal();
	}
}
