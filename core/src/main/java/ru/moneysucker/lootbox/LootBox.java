package ru.moneysucker.lootbox;

import lombok.Data;
import org.lineage.commons.util.Rnd;
import org.lineage.gameserver.datatables.ItemTable;
import org.lineage.gameserver.engines.items.ItemDataHolder;
import org.lineage.gameserver.enums.ItemGrade;
import org.lineage.gameserver.model.holders.ItemHolder;
import org.lineage.gameserver.model.items.Armor;
import org.lineage.gameserver.model.items.EtcItem;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.items.Weapon;
import org.lineage.gameserver.model.items.instance.ItemInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Top Grade Life Stone
 * Mid Grade Life Stone
 * Epic Jewel
 * Enchant Skill Book
 * Suit Dress
 * Armor S +
 * Weapon S +
 * Jewel S +
 * @author finfan
 */
@Data
public class LootBox {

	public static final int MAX_DROP_RATE = 10000;

	private final LootBoxType boxType;
	private final Map<Item, Integer> items = new HashMap<>();

	public void fill() {
		//epic jewel with 0.01%
		switch (boxType) {
			case DINO:
				items.put(ItemTable.getInstance().getTemplate(1), getChance(0.01));
				items.put(ItemTable.getInstance().getTemplate(2), getChance(0.01));
				items.put(ItemTable.getInstance().getTemplate(2), getChance(0.01));
				break;

			case KETRA:
				break;

			case VARKA:
				break;
		}

		// lifestone drop from price
		EtcItem lifeStone = Rnd.get(ItemTable.getInstance().getLifeStones().values());
		items.put(lifeStone, Math.min(5000, MAX_DROP_RATE / lifeStone.getReferencePrice() * MAX_DROP_RATE));

		// enchant skill book 15%
		items.put(ItemTable.getInstance().getTemplate(6622), getChance(0.15));

		// suit dresses

		// armor S+
		List<Armor> armors = ItemTable.getInstance().getArmorGrades().get(ItemGrade.S);
		Armor armor = Rnd.get(armors);
		items.put(armor, getChance(0.45));

		// weapon S+
		List<Weapon> weapons = ItemTable.getInstance().getWeaponGrades().get(ItemGrade.S);
		Weapon weapon = Rnd.get(weapons);
		items.put(weapon, getChance(0.22));

		// jewel S+
	}

	public void openUp() {
		//TODO: открываем ящик
	}

	public void onDrop() {
		fill();
	}

	public static int getChance(double inDouble) {
		return (int) (inDouble * MAX_DROP_RATE);
	}
}
