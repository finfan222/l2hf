package ru.moneysucker.lootbox;

import lombok.Data;

/**
 * @author finfan
 */
@Data
public class LootBoxKey {

	private final KeyType keyType;

	public double onOpen(int modifier) {
		return modifier * keyType.getModifier();
	}
}
