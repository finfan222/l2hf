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

import org.lineage.gameserver.geoengine.GeoEngine;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.skills.Skill;

/**
 * @author Mobius
 */
public class ConditionMinDistance extends Condition
{
	private final int _distance;
	
	public ConditionMinDistance(int distance)
	{
		_distance = distance;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		return (effected != null) //
			&& (effector.calculateDistance3D(effected) >= _distance) //
			&& GeoEngine.getInstance().canSeeTarget(effector, effected);
	}
}
