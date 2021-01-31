package ru.mmorpg.gameplay.items.enums;

import ru.mmorpg.gameplay.inventory.AbstractInventory;
import ru.mmorpg.gameplay.items.Item;

/**
 * Устанавливается при:
 * <ul>
 *     <li>Удалении вещи {@link AbstractInventory#deleteItem(int)} или {@link AbstractInventory#deleteItem(Item)}</li>
 *     <li>Изменении функционала вещи или её кол-ва {@link Item#decrease(long)} или {@link Item#increase(long)} (long)}</li>
 *     <li>Добавлении новой вещи {@link AbstractInventory#add(int)} или {@link AbstractInventory#deleteItem(Item)}</li>
 * </ul>
 * Установка должна происходить только в {@link AbstractInventory#deleteItem(int)} или {@link AbstractInventory#deleteItem(Item)}
 * @author finfan
 */
public enum ItemManipulation {
	UNCHANGED,
	ADD,
	REMOVE,
	MODIFY;
}
