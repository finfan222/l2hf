package ru.mmorpg.gameplay.inventory;

import org.jetbrains.annotations.NotNull;
import org.lineage.gameserver.enums.ItemLocation;
import ru.mmorpg.gameplay.ResultType;
import ru.mmorpg.gameplay.items.Container;
import ru.mmorpg.gameplay.items.Item;

import java.util.Set;

/**
 * @author finfan
 */
public abstract class AbstractInventory {

	protected final Container container;

	protected AbstractInventory(Container container) {
		this.container = container;
	}

	public final Set<Item> getItems() {
		return container.getItems();
	}

	public final Set<Item> getAllItemsBy(int id) {
		return container.getAllItemsBy(id);
	}

	public abstract Item getItem(int uuid);

	public abstract ResultType addItem(Item item);

	public abstract ResultType addItem(int id, long count);

	public abstract ResultType deleteItem(int uuid);

	public abstract ResultType deleteItem(Item item);

	public abstract ResultType decreaseCount(int uuid, long count);

	public abstract ResultType increaseCount(int uuid, long count);

	public ItemLocation getLocation() {
		return ItemLocation.VOID;
	}

	public abstract void update(@NotNull(value = "Обновление инвентаря не может быть без Item") Item... items);
}
