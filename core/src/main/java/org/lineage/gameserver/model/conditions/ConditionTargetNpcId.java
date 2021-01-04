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

import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.skills.Skill;

import java.util.List;

/**
 * The Class ConditionTargetNpcId.
 */
public class ConditionTargetNpcId extends Condition
{
	private final List<Integer> _npcIds;
	
	/**
	 * Instantiates a new condition target npc id.
	 * @param npcIds the npc ids
	 */
	public ConditionTargetNpcId(List<Integer> npcIds)
	{
		_npcIds = npcIds;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		final WorldObject target = (effector == null) || !effector.isPlayer() ? effected : effector.getTarget();
		return (target != null) && (target.isNpc() || target.isDoor()) && _npcIds.contains(target.getId());
	}
}
