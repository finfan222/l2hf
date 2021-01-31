package ru.mmorpg.gameplay.inventory;

import lombok.extern.slf4j.Slf4j;
import org.lineage.gameserver.enums.ItemLocation;
import org.lineage.gameserver.network.serverpackets.InventoryUpdate;
import ru.mmorpg.gameplay.ResultType;
import ru.mmorpg.gameplay.factory.ItemFactory;
import ru.mmorpg.gameplay.items.Container;
import ru.mmorpg.gameplay.items.Item;
import ru.mmorpg.gameplay.items.enums.ItemManipulation;
import ru.mmorpg.gameplay.items.enums.PartType;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author finfan
 */
@Slf4j
public class Bag extends AbstractInventory {

	//private final Creature owner;
	private final ReentrantLock reentrantLock = new ReentrantLock(true);
	private final Item[] equipment = new Item[PartType.values().length];

	public Bag(Container container /*Creature owner*/) {
		super(container);
		//this.owner = owner;
	}

	/**
	 * Одеваем вещь в один из {@link PartType} слотов.
	 * <ul>
	 *     <li>Если вещь !{@code isEquippable} - негативный результат</li>
	 *     <li>Если {@link PartType} экипируемой вещи - null, то возвращаем негативный результат</li>
	 *     <li>Если {@code itemEntity} уже {@code isEquipped} то вызываем метод {@link #unEquip(PartType)}</li>
	 *     <li>В остальных случая экипируем вещь</li>
	 * </ul>
	 *
	 * @param item вещь которую одеваем
	 * @return {@link ResultType}
	 */
	public ResultType equip(Item item) {
		if (!item.isEquippable()) {
			return ResultType.FAILED;
		}

		if (item.getPartType() == null) {
			return ResultType.FAILED;
		}

		if (item.isEquipped()) {
			unEquip(item.getPartType());
		} else {
			// получаем partType вещи
			PartType partType = item.getPartType();
			// устанавливаем itemEntity в ячейку снаряжения
			equipment[partType.order()] = item;
			// устанавливаем локацию хранения
			equipment[partType.order()].setItemLocation(ItemLocation.PAPERDOLL);
			log.info("{} экипирована в слот {}", item, partType);
		}

		return ResultType.SUCCESS;
	}

	public ResultType unEquip(PartType partType) {
		Item partItem = this.equipment[partType.order()];

		// если в слоте ничего не экипировано
		if(partItem == null) {
			return ResultType.FAILED;
		}

		// если вещь нельзя снять
		if(partItem.isBlocked()) {
			return ResultType.FAILED;
		}

		final String name = partItem.getData().getName();
		this.equipment[partType.order()].setItemLocation(getLocation());
		this.equipment[partType.order()] = null;
		log.info("{} снята со слота {}", name, partType);
		return ResultType.SUCCESS;
	}

	public Item getPart(PartType partType) {
		return equipment[partType.order()];
	}

	@Override
	public Item getItem(int uuid) {
		Item entity = container.find(uuid);
		if(entity == null) {
			return null;
		}

		return entity;
	}

	@Override
	public ResultType addItem(Item item) {
		if (item == null) {
			return ResultType.FAILED;
		}

		if (container.add(item).isFailed()) {
			//TODO: сообщение о том что неудачная попытка добавления вещи
			return ResultType.FAILED;
		}

		item.setLastManipulation(ItemManipulation.ADD);
		return ResultType.SUCCESS;
	}

	@Override
	public ResultType addItem(int id, long count) {
		Item item = ItemFactory.getInstance().create(id, count);
		if (item == null) {
			return ResultType.FAILED;
		}

		if (container.add(item).isFailed()) {
			//TODO: сообщение о том что неудачная попытка добавления вещи
			return ResultType.FAILED;
		}

		item.setLastManipulation(ItemManipulation.ADD);
		return ResultType.SUCCESS;
	}

	@Override
	public ResultType deleteItem(int uuid) {
		Item item = getItem(uuid);
		if (item == null) {
			return ResultType.FAILED;
		}

		if (item.isEquipped()) {
			unEquip(item.getPartType());
		}

		ResultType result = container.remove(item);
		if (result.isFailed()) {
			//TODO: неудачное удаление вещи
			return ResultType.FAILED;
		}
		return result;
	}

	@Override
	public ResultType deleteItem(Item item) {
		return deleteItem(item.getUuid());
	}

	@Override
	public ResultType decreaseCount(int uuid, long count) {
		Item item = getItem(uuid);
		if(item == null) {
			return ResultType.FAILED;
		}

		if (item.decrease(count) == ItemManipulation.REMOVE) {
			deleteItem(item);
		}
		return ResultType.SUCCESS;
	}

	public ResultType decreaseCount(Item item, long count) {
		return decreaseCount(item.getUuid(), count);
	}

	@Override
	public ResultType increaseCount(int uuid, long count) {
		Item item = getItem(uuid);
		if(item == null) {
			return ResultType.FAILED;
		}

		if(item.getCount().isOverflowed(count)) {
			return ResultType.FAILED;
		}

		item.increase(count);
		return ResultType.SUCCESS;
	}

	@Override
	public ItemLocation getLocation() {
		return ItemLocation.INVENTORY;
	}

	@Override
	public void update(Item... items) {
		InventoryUpdate iu = new InventoryUpdate();
		for(Item i : items) {
			//iu.addItem(i);
			//TODO: addItem/removeItem/ModifyItem
			// должны учитывать lastManipulation
		}
		//TODO: owner.sendPacket
	}
}
