package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum PartType implements Enumerate {

	NONE,
	UNDER,
	HEAD,
	HAIR,
	HAIR2,
	NECK,
	RIGHT_HAND,
	CHEST,
	LEFT_HAND,
	REAR,
	LEAR,
	GLOVES,
	LEGS,
	FEET,
	RIGHT_FINGER,
	LEFT_FINGER,
	LEFT_BRACELET,
	RIGHT_BRACELET,
	DECO1,
	DECO2,
	DECO3,
	DECO4,
	DECO5,
	DECO6,
	CLOAK,
	BELT;

	@Override
	public PartType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public PartType[] array() {
		PartType[] active = new PartType[values().length - 1];
		System.arraycopy(values(), 1, active, 0, values().length - 1);
		return active;
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
