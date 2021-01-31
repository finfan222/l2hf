package ru.mmorpg.gameplay.factory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lineage.gameserver.enums.ItemLocation;
import ru.mmorpg.gameplay.items.Item;
import ru.mmorpg.gameplay.items.ItemData;

import java.util.HashSet;

/**
 * @author finfan
 */
@Slf4j
public final class ItemFactory extends AbstractFactory {

	@Getter private static final ItemFactory instance = new ItemFactory();

	private ItemFactory() {
		createdInstances.put(Item.class, new HashSet<>());
	}

	@Override
	public Item create(Object... args) {
		Item createdItem = null;
		try {
			Integer id = (Integer) args[0];
			Long count = (Long) args[1];

			// ищем дату
			ItemData data = new ItemData();
			data.setId(id);

			// создаём инстанцию вещи
			//TODO: генерация uuid для создаваемой сущности
			createdItem = new Item(1, data, ItemLocation.VOID, count);

			// добавляем в созданные инстанции
			createdInstances.get(Item.class).add(createdItem);
		} catch (Exception e) {
			log.error("Ошибка пр исоздании Item.", e);
		}
		return createdItem;
	}

	@Override
	public void destroy(Object... args) {

	}
}
