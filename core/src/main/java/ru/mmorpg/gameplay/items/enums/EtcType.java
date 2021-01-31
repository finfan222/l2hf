package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum EtcType implements Enumerate {
	NONE,
	/**
	 * Снаряд для луков
	 */
	ARROW,
	/**
	 * Снаряд для арбалетов
	 */
	BOLT,
	/**
	 * Особенный напиток типа зелий
	 */
	POTION,
	/**
	 * Особенный напиток типа эликсиров
	 */
	ELIXIR,
	/**
	 * Свиток который можно почитать.
	 */
	SCROLL,
	/**
	 * Книга которую можно прочитать
	 */
	BOOK,
	/**
	 * Минерал, который можно сломать или поглотить. Кристалы, руны и т.д.
	 */
	MINERAL,
	/**
	 * Валюта мира
	 */
	CURRENCY,
	/**
	 * Предмет который позволяет что-то улучшать. Оружие заточка, заклинание заточка - поднятие лвл?
	 */
	ENHANCEMENT;

	@Override
	public EtcType get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public EtcType[] array() {
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
