package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum RestrictionType implements Enumerate {
	NON_TRADE, // нельзя торговат ьс гиркоами
	NON_STORE, // нельзя хранить взранилищах
	NON_PERSISTABLE, // нельзя созранят ьв дб
	NON_DROP, // нельзя дропать
	NON_SELL, // нельзя продавать NPC
	NON_ENCHANT, // нельзя улучшать
	NON_DESTROY, // нельзя уничтожить
	NON_DEPOSIT; // нельзя передават ьчарам на аке

	@Override
	public RestrictionType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public RestrictionType[] array() {
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
