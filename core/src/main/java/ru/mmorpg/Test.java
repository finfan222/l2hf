package ru.mmorpg;


import lombok.extern.slf4j.Slf4j;
import org.lineage.gameserver.enums.ItemLocation;
import ru.mmorpg.gameplay.inventory.Bag;
import ru.mmorpg.gameplay.items.Container;
import ru.mmorpg.gameplay.items.Item;
import ru.mmorpg.gameplay.items.ItemData;
import ru.mmorpg.gameplay.items.enums.EtcType;
import ru.mmorpg.gameplay.items.enums.PartType;
import ru.mmorpg.gameplay.items.enums.SlotType;
import ru.mmorpg.gameplay.items.enums.WeaponType;
import ru.mmorpg.structure.Num;

/**
 * @author finfan
 */
@Slf4j
public class Test {
	public static void main(String[] args) {
		Container container = new Container();
		container.setCapacity(new Num(20));
		container.setWeight(new Num());
		Bag bag = new Bag(container);

		ItemData data = new ItemData();
		data.setWeight(2.25f);
		data.setId(1);
		data.setName("Test Item");
		data.setStackable(true);
		data.setGroupType(EtcType.NONE);

		Item item = new Item(1, data, ItemLocation.VOID, 15);

		bag.addItem(item);
		System.out.println(item.getCount());
		bag.decreaseCount(item, 14);
		System.out.println(item.getCount());
		bag.decreaseCount(item, 14);
		System.out.println(bag.getItems().size());
		bag.getItem(item.getUuid());

		ItemData sword = new ItemData();
		sword.setWeight(13.2f);
		sword.setId(2);
		sword.setName("Sword");
		sword.setGroupType(WeaponType.SWORD);
		sword.setPartType(PartType.RIGHT_HAND);
		sword.setSlotType(SlotType.RIGHT_HAND);

		Item swordEntity = new Item(2, sword, ItemLocation.VOID, 1);
		bag.equip(swordEntity);
		System.out.println(bag.getPart(PartType.RIGHT_HAND).getData().getName());
		bag.unEquip(PartType.RIGHT_HAND);
		System.out.println(bag.getPart(PartType.RIGHT_HAND));
	}
}
