package ru.moneysucker.lootbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author finfan
 */
@AllArgsConstructor
@Getter
public enum LootBoxType {
	DINO(50),
	KETRA(50),
	VARKA(50);

	private final int dropChance;
}
