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
package org.lineage.gameserver.model.events.impl.creature;

import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.events.EventType;
import org.lineage.gameserver.model.events.impl.IBaseEvent;
import org.lineage.gameserver.model.skills.Skill;

/**
 * An instantly executed event when Creature is attacked by Creature.
 * @author UnAfraid
 */
public class OnCreatureSkillUse implements IBaseEvent
{
	private final Creature _caster;
	private final Skill _skill;
	private final boolean _simultaneously;
	private final Creature _target;
	private final WorldObject[] _targets;
	
	public OnCreatureSkillUse(Creature caster, Skill skill, boolean simultaneously, Creature target, WorldObject[] targets)
	{
		_caster = caster;
		_skill = skill;
		_simultaneously = simultaneously;
		_target = target;
		_targets = targets;
	}
	
	public Creature getCaster()
	{
		return _caster;
	}
	
	public Skill getSkill()
	{
		return _skill;
	}
	
	public boolean isSimultaneously()
	{
		return _simultaneously;
	}
	
	public Creature getTarget()
	{
		return _target;
	}
	
	public WorldObject[] getTargets()
	{
		return _targets;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_CREATURE_SKILL_USE;
	}
}