package ru.mmorpg.gameplay.inventory;

import org.lineage.gameserver.enums.ItemLocation;
import ru.mmorpg.gameplay.ResultType;
import ru.mmorpg.gameplay.items.Container;
import ru.mmorpg.gameplay.items.Item;

/**
 * @author finfan
 */
public class Warehouse extends AbstractInventory {

	public Warehouse(Container container) {
		super(container);
	}

	@Override
	public Item getItem(int uuid) {
		return null;
	}

	@Override
	public ResultType addItem(Item item) {
		return null;
	}

	@Override
	public ResultType addItem(int id, long count) {
		return null;
	}

	@Override
	public ResultType deleteItem(int uuid) {
		return null;
	}

	@Override
	public ResultType deleteItem(Item item) {
		return null;
	}

	@Override
	public ResultType decreaseCount(int uuid, long count) {
		return null;
	}

	@Override
	public ResultType increaseCount(int uuid, long count) {
		return null;
	}

	@Override
	public ItemLocation getLocation() {
		return ItemLocation.WAREHOUSE;
	}
}
