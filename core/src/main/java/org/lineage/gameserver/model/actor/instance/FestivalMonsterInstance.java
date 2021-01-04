/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.lineage.gameserver.model.actor.instance;

import org.lineage.gameserver.SevenSignsFestival;
import org.lineage.gameserver.enums.InstanceType;
import org.lineage.gameserver.model.Party;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.templates.NpcTemplate;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.network.serverpackets.InventoryUpdate;

/**
 * This class manages all attackable festival NPCs, spawned during the Festival of Darkness.
 * @author Tempy
 */
public class FestivalMonsterInstance extends MonsterInstance
{
	protected int _bonusMultiplier = 1;
	
	/**
	 * Creates a festival monster.
	 * @param template the festival monster NPC template
	 */
	public FestivalMonsterInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.FestivalMonsterInstance);
	}
	
	public void setOfferingBonus(int bonusMultiplier)
	{
		_bonusMultiplier = bonusMultiplier;
	}
	
	/**
	 * Return True if the attacker is not another FestivalMonsterInstance.
	 */
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		return !(attacker instanceof FestivalMonsterInstance);
	}
	
	/**
	 * All mobs in the festival are aggressive, and have high aggro range.
	 */
	@Override
	public boolean isAggressive()
	{
		return true;
	}
	
	/**
	 * All mobs in the festival really don't need random animation.
	 */
	@Override
	public boolean hasRandomAnimation()
	{
		return false;
	}
	
	/**
	 * Actions:
	 * <ul>
	 * <li>Check if the killing object is a player, and then find the party they belong to.</li>
	 * <li>Add a blood offering item to the leader of the party.</li>
	 * <li>Update the party leader's inventory to show the new item addition.</li>
	 * </ul>
	 */
	@Override
	public void doItemDrop(Creature lastAttacker)
	{
		PlayerInstance killingChar = null;
		if (!lastAttacker.isPlayer())
		{
			return;
		}
		
		killingChar = (PlayerInstance) lastAttacker;
		final Party associatedParty = killingChar.getParty();
		if (associatedParty == null)
		{
			return;
		}
		
		final PlayerInstance partyLeader = associatedParty.getLeader();
		final ItemInstance addedOfferings = partyLeader.getInventory().addItem("Sign", SevenSignsFestival.FESTIVAL_OFFERING_ID, _bonusMultiplier, partyLeader, this);
		final InventoryUpdate iu = new InventoryUpdate();
		if (addedOfferings.getCount() != _bonusMultiplier)
		{
			iu.addModifiedItem(addedOfferings);
		}
		else
		{
			iu.addNewItem(addedOfferings);
		}
		partyLeader.sendPacket(iu);
		
		super.doItemDrop(lastAttacker); // Normal drop
	}
}