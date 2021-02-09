package ru.moneysucker.lootbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author finfan
 */
@AllArgsConstructor
@Getter
public enum KeyType {
	BRONZE(1.0000),
	SILVER(2.0000),
	GOLD(3.0000);

	private final double modifier;
}
