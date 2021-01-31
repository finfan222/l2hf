package ru.mmorpg.gameplay.items;

import lombok.Data;
import org.lineage.gameserver.model.items.type.MaterialType;
import ru.mmorpg.gameplay.items.enums.*;
import ru.mmorpg.structure.enums.Enumerate;

/**
 * @author finfan
 */
@Data
public class ItemData {
	private int id;
	private int displayId;
	private String name;
	private String icon;
	private ItemType1 itemType1;
	private ItemType2 itemType2;
	private Enumerate groupType = EtcType.NONE;
	private long buyPrice;
	private long sellPrice;
	private float sellTaxRate; // 0.25 on sale you get only 75% from selLPrice
	private float weight; // 52.2kg = 52200, 0.01kg = 10, 0.1 = 100, 1 = 1000
	private PartType partType = PartType.NONE;
	private SlotType slotType = SlotType.NONE;
	private boolean sellable;
	private boolean dropable;
	private boolean destroyable;
	private boolean tradeable;
	private boolean depositable;
	private int enchantable;
	private boolean freightable;
	private long durability;
	private long duration;
	private boolean stackable;
	private Materials[] materials;

	@Data
	public static class Materials {
		private MaterialType materialType;
		private int count;
	}
}
