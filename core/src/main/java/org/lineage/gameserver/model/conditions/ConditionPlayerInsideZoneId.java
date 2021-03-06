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

import org.lineage.gameserver.instancemanager.ZoneManager;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.model.zone.ZoneType;

import java.util.List;

/**
 * @author UnAfraid
 */
public class ConditionPlayerInsideZoneId extends Condition
{
	private final List<Integer> _zones;
	
	public ConditionPlayerInsideZoneId(List<Integer> zones)
	{
		_zones = zones;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.getActingPlayer() == null)
		{
			return false;
		}
		
		for (ZoneType zone : ZoneManager.getInstance().getZones(effector))
		{
			if (_zones.contains(zone.getId()))
			{
				return true;
			}
		}
		return false;
	}
}
