package ru.mmorpg.gameplay.items;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mmorpg.gameplay.ResultType;
import ru.mmorpg.gameplay.items.enums.ItemManipulation;
import ru.mmorpg.gameplay.items.enums.PartType;
import ru.mmorpg.structure.Num;
import ru.mmorpg.utils.ConcurrencyUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author finfan
 */
@Slf4j
@Data
@NoArgsConstructor
public class Container {

	private final ReentrantLock reentrantLock = new ReentrantLock(true);

	private final Set<Item> items = new HashSet<>();
	private final Set<Item> armors = new HashSet<>();
	private final Set<Item> weapons = new HashSet<>();
	private final Set<Item> jewel = new HashSet<>();

	private Num capacity;
	private Num weight;

	public boolean hasFreeSpace() {
		return items.size() < capacity.intValue();
	}

	public boolean hasOverweight(ItemData item) {
		return weight.isOverflowed(item.getWeight());
	}

	private void reserve(Item item) {
		switch(item.getLastManipulation()) {
			case ADD:
				if (item.isArmor()) {
					armors.add(item);
				} else if (item.isWeapon()) {
					weapons.add(item);
				} else if (item.isJewel()) {
					jewel.add(item);
				}
				items.add(item);
				break;

			case REMOVE:
				if (item.isArmor()) {
					armors.remove(item);
				} else if (item.isWeapon()) {
					weapons.remove(item);
				} else if (item.isJewel()) {
					jewel.remove(item);
				}
				items.remove(item);
				break;
		}
	}

	public ResultType add(Item item) {
		// больше нет мест
		if (!hasFreeSpace()) {
			return ResultType.FAILED;
		}

		// недостаточно веса для добавления вещи
		if (hasOverweight(item.getData())) {
			return ResultType.FAILED;
		}

		// попытка добавить вещь
		boolean tryAdd = ConcurrencyUtils.callLock(reentrantLock, () -> {
			weight.add(item.getTotalWeight());
			item.setLastManipulation(ItemManipulation.ADD);
			reserve(item);
		}, () -> log.error("Ошибка при добавлении {} в контейнер.", item));

		return !tryAdd ? ResultType.FAILED : ResultType.SUCCESS;
	}

	public ResultType remove(Item item) {
		boolean tryRm = ConcurrencyUtils.callLock(reentrantLock, () -> {
			weight.sub(item.getTotalWeight());
			item.setLastManipulation(ItemManipulation.REMOVE);
			reserve(item);
		}, () -> log.error("Ошибка при удалении {} из контейнера."));

		return !tryRm ? ResultType.FAILED : ResultType.SUCCESS;
	}

	public ResultType remove(int uuid) {
		Item item = find(uuid);
		if (item == null) {
			return ResultType.FAILED;
		}

		return remove(item);
	}

	public ResultType increase(int id, long count) {
		Item item = find(id);
		if (item == null) {
			return ResultType.FAILED;
		}

		boolean tryInc = ConcurrencyUtils.callLock(reentrantLock, () -> {
			item.increase(count);
		}, () -> log.error("Ошибка при увеличении count переменной в ItemEntity."));

		return !tryInc ? ResultType.FAILED : ResultType.SUCCESS;
	}

	public ResultType decrease(int id, long count) {
		Item item = find(id);
		if (item == null) {
			return ResultType.FAILED;
		}

		boolean tryInc = ConcurrencyUtils.callLock(reentrantLock, () -> {
			item.decrease(count);
		}, () -> log.error("Ошибка при увеличении count переменной в ItemEntity."));

		return !tryInc ? ResultType.FAILED : ResultType.SUCCESS;
	}

	public Item find(int uuid) {
		for (Item e : items) {
			if (e.validate(uuid)) {
				return e;
			}
		}

		return null;
	}

	/**
	 * Вовзращает {@link Set} всех вещей которые есть у персонажа по их {@code ID}.
	 * <ul>
	 *     <li>Исключает вещи которые экипированы</li>
	 * </ul>
	 * @param id
	 * @return
	 */
	public Set<Item> getAllItemsBy(int id) {
		final Set<Item> list = new HashSet<>();
		for (Item e : items) {
			if (e.getId() == id && !e.isEquipped()) {
				list.add(e);
			}
		}
		return list;
	}

	public Set<Item> getAllItemsBy(PartType partType) {
		final Set<Item> list = new HashSet<>();
		for (Item e : items) {
			if (e.getData().getPartType() == partType && !e.isEquipped()) {
				list.add(e);
			}
		}
		return list;
	}

	public void increaseCapacity(int add) {
		capacity.add(add);
	}

	public void increaseWeight(long add) {
		weight.add(add);
	}

}
