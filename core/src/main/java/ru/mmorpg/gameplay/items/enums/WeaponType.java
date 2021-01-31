package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum WeaponType implements Enumerate {
	SWORD(new DamageType[]{DamageType.CUT, DamageType.PIERCE}),
	HAMMER(new DamageType[]{DamageType.CRUSH}),
	AXE(new DamageType[]{DamageType.SLASH, DamageType.CUT}),
	DAGGER(new DamageType[]{DamageType.CUT, DamageType.PIERCE}),
	POLE(new DamageType[]{DamageType.CUT, DamageType.PIERCE, DamageType.SLASH}),
	FISTS(true, new DamageType[]{DamageType.CRUSH}),
	KNUCKLES(true, new DamageType[]{DamageType.CRUSH}),
	BOW(true),
	CROSSBOW(true),
	STAFF(true, new DamageType[]{DamageType.CRUSH, DamageType.MAGIC}),
	WAND(new DamageType[]{DamageType.CRUSH, DamageType.MAGIC}),
	BOOK(new DamageType[]{DamageType.CRUSH, DamageType.MAGIC}),
	SCROLL,
	FISHING_ROD(true, new DamageType[]{DamageType.BITING}),
	RAPIER(new DamageType[]{DamageType.PIERCE});

	private final boolean is2handed;
	private final DamageType[] damageType;

	WeaponType(boolean is2handed, DamageType[] damageTypes) {
		this.is2handed = is2handed;
		this.damageType = damageTypes;
	}

	WeaponType(DamageType[] damageTypes) {
		this(false, damageTypes);
	}

	WeaponType(boolean is2handed) {
		this(is2handed, null);
	}

	WeaponType() {
		this(false, null);
	}

	@Override
	public WeaponType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public WeaponType[] array() {
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
