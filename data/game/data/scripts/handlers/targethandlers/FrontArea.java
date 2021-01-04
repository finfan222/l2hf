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
package handlers.targethandlers;

import java.util.ArrayList;
import java.util.List;

import org.lineage.gameserver.handler.ITargetTypeHandler;
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.model.skills.targets.TargetType;
import org.lineage.gameserver.model.zone.ZoneId;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class FrontArea implements ITargetTypeHandler
{
	@Override
	public WorldObject[] getTargetList(Skill skill, Creature creature, boolean onlyFirst, Creature target)
	{
		final List<Creature> targetList = new ArrayList<>();
		if ((target == null) || (((target == creature) || target.isAlikeDead()) && (skill.getCastRange() >= 0)) || (!(target.isAttackable() || target.isPlayable())))
		{
			creature.sendPacket(SystemMessageId.THAT_IS_AN_INCORRECT_TARGET);
			return EMPTY_TARGET_LIST;
		}
		
		final Creature origin;
		final boolean srcInArena = (creature.isInsideZone(ZoneId.PVP) && !creature.isInsideZone(ZoneId.SIEGE));
		if (skill.getCastRange() >= 0)
		{
			if (!Skill.checkForAreaOffensiveSkills(creature, target, skill, srcInArena))
			{
				return EMPTY_TARGET_LIST;
			}
			
			if (onlyFirst)
			{
				return new Creature[]
				{
					target
				};
			}
			
			origin = target;
			targetList.add(origin); // Add target to target list
		}
		else
		{
			origin = creature;
		}
		
		// Update heading towards the origin
		if (creature != origin)
		{
			creature.setHeading(Util.calculateHeadingFrom(creature, origin));
		}
		
		final int maxTargets = skill.getAffectLimit();
		World.getInstance().forEachVisibleObject(creature, Creature.class, obj ->
		{
			if (!(obj.isAttackable() || obj.isPlayable()))
			{
				return;
			}
			
			if (obj == origin)
			{
				return;
			}
			
			if (Util.checkIfInRange(skill.getAffectRange(), origin, obj, true))
			{
				if (!obj.isInFrontOf(creature))
				{
					return;
				}
				
				if (!Skill.checkForAreaOffensiveSkills(creature, obj, skill, srcInArena))
				{
					return;
				}
				
				if ((maxTargets > 0) && (targetList.size() >= maxTargets))
				{
					return;
				}
				
				targetList.add(obj);
			}
		});
		
		if (targetList.isEmpty())
		{
			return EMPTY_TARGET_LIST;
		}
		
		return targetList.toArray(new Creature[targetList.size()]);
	}
	
	@Override
	public Enum<TargetType> getTargetType()
	{
		return TargetType.FRONT_AREA;
	}
}