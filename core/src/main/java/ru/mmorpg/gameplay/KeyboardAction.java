package ru.mmorpg.gameplay;

import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.utils.TextUtils;

/**
 * @author finfan
 */
public enum KeyboardAction implements Enumerate {
	NORMAL,
	SHIFT,
	CTRL;

	@Override
	public KeyboardAction get(String enumName) {
		return valueOf(enumName.toUpperCase());
	}

	@Override
	public KeyboardAction[] array() {
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
