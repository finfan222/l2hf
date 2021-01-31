package ru.mmorpg.gameplay.items;

import org.lineage.gameserver.enums.ItemLocation;
import ru.mmorpg.gameplay.items.enums.ItemManipulation;
import ru.mmorpg.gameplay.items.enums.ItemType1;
import ru.mmorpg.gameplay.items.enums.ItemType2;
import ru.mmorpg.utils.Utils;

/**
 * @author finfan
 */
public class ItemUpdateData {
	private int uuid;
	private ItemData data;
	private int enchant;
	private int augmentation;
	private long count;
	private long price;
	private ItemType1 type1;
	private ItemType2 type2;
	private int isEquipped;
	private ItemManipulation lastManipulation; // Get the action to do clientside
	private int durability;
	private int duration;
	private ItemLocation location;
	private int elemAtkType = -2;
	private int elemAtkPower = 0;
	private final int[] elemDefAttr = {
		0,
		0,
		0,
		0,
		0,
		0
	};

	private int[] options;

	public ItemUpdateData(Item item) {
		uuid = item.getUuid();
		data = item.getData();
		enchant = item.getEnchant().shortValue();
		augmentation = item.isAugmented() ? item.getAugmentation().getAugmentationId() : 0;
		count = item.getCount().longValue();
		type1 = item.getType1();
		type2 = item.getType2();
		isEquipped = Utils.boolToInt(item.isEquipped());
		lastManipulation = item.getLastManipulation();
		durability = item.getDurability().toSeconds().intValue();
		duration = item.getDuration().toSeconds().intValue();
		location = item.getItemLocation();
		elemAtkType = item.getAttackElementType();
		elemAtkPower = item.getAttackElementPower();
		for (byte i = 0; i < 6; i++) {
			elemDefAttr[i] = item.getElementDefAttr(i);
		}
		options = item.getEnchantOptions();
	}

	public ItemUpdateData(Item item, ItemManipulation itemManipulation) {
		this(item);
		this.lastManipulation = itemManipulation;
	}

	/*public ItemUpdateData(TradeItem item) {
		uuid = item.getObjectId();
		_item = item.getItem();
		_enchant = item.getEnchant();
		_augmentation = 0;
		_count = item.getCount();
		_type1 = item.getCustomType1();
		_type2 = item.getCustomType2();
		_equipped = 0;
		_change = 0;
		durability = -1;
		duration = ItemConstant.BASE_DURATION;
		_location = item.getLocationSlot();
		_elemAtkType = item.getAttackElementType();
		_elemAtkPower = item.getAttackElementPower();
		for (byte i = 0; i < 6; i++) {
			_elemDefAttr[i] = item.getElementDefAttr(i);
		}

		_option = item.getEnchantOptions();
	}*/
}
