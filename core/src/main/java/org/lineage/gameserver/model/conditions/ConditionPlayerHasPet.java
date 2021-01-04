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
package org.lineage.gameserver.model.conditions;

import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.instance.PetInstance;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.model.skills.Skill;

import java.util.List;

/**
 * The Class ConditionPlayerHasPet.
 */
public class ConditionPlayerHasPet extends Condition
{
	private final List<Integer> _controlItemIds;
	
	/**
	 * Instantiates a new condition player has pet.
	 * @param itemIds the item ids
	 */
	public ConditionPlayerHasPet(List<Integer> itemIds)
	{
		if ((itemIds.size() == 1) && (itemIds.get(0) == 0))
		{
			_controlItemIds = null;
		}
		else
		{
			_controlItemIds = itemIds;
		}
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effector.getActingPlayer() == null) || (!(effector.getActingPlayer().getSummon() instanceof PetInstance)))
		{
			return false;
		}
		
		if (_controlItemIds == null)
		{
			return true;
		}
		
		final ItemInstance controlItem = ((PetInstance) effector.getActingPlayer().getSummon()).getControlItem();
		return (controlItem != null) && _controlItemIds.contains(controlItem.getId());
	}
}
