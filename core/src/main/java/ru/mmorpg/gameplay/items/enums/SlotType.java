package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum SlotType implements Enumerate {

	NONE(0x0000),
	UNDERWEAR(0x0001),
	RIGHT_EAR(0x0002),
	LEFT_EAR(0x0004),
	LEFT_RIGHT_EAR(0x00006),
	NECK(0x0008),
	RIGHT_FINGER(0x0010),
	LEFT_FINGER(0x0020),
	LEFT_RIGHT_FINGER(0x0030),
	HEAD(0x0040),
	RIGHT_HAND(0x0080),
	LEFT_HAND(0x0100),
	GLOVES(0x0200),
	CHEST(0x0400),
	LEGS(0x0800),
	FEET(0x1000),
	BACK(0x2000),
	LEFT_RIGHT_HAND(0x4000),
	FULL_ARMOR(0x8000),
	HAIR(0x010000),
	SUIT(0x020000),
	HAIR2(0x040000),
	FULL_HAIR(0x080000),
	RIGHT_BRACELET(0x100000),
	LEFT_BRACELET(0x200000),
	DECO(0x400000),
	BELT(0x10000000),
	WOLF(-100),
	HATCHLING(-101),
	STRIDER(-102),
	BABY_PET(-103),
	GREAT_WOLF(-104),
	FULL_WEAPON(LEFT_RIGHT_HAND.mask | RIGHT_HAND.mask);

	private final int mask;

	SlotType(int mask) {
		this.mask = mask;
	}

	@Override
	public int personal() {
		return mask;
	}

	@Override
	public SlotType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public SlotType[] array() {
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

	public boolean isJewel() {
		switch(this) {
			case RIGHT_EAR:
			case RIGHT_FINGER:
			case LEFT_EAR:
			case LEFT_FINGER:
			case NECK:
				return true;
		}

		return false;
	}
}
