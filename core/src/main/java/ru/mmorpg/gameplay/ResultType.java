package ru.mmorpg.gameplay;

/**
 * @author finfan
 */
public enum ResultType {
	FAILED,
	SUCCESS,
	UNCONTESTED;

	public final boolean isFailed() {
		return this == FAILED;
	}
}
