package ru.mmorpg.gameplay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author finfan
 */
@AllArgsConstructor
public abstract class AbstractEntity {
	@Getter protected final int uuid;

	public final boolean validate(int uuid) {
		return this.uuid == uuid;
	}
}
