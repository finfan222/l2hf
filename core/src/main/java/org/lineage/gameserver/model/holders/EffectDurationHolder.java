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
package org.lineage.gameserver.model.holders;

import org.lineage.gameserver.model.skills.Skill;

/**
 * Effect duration holder.
 * @author Zoey76
 */
public class EffectDurationHolder
{
	private final int _skillId;
	private final int _skillLevel;
	private final int _duration;
	
	/**
	 * Effect duration holder constructor.
	 * @param skill the skill to get the data
	 * @param duration the effect duration
	 */
	public EffectDurationHolder(Skill skill, int duration)
	{
		_skillId = skill.getDisplayId();
		_skillLevel = skill.getDisplayLevel();
		_duration = duration;
	}
	
	/**
	 * Get the effect's skill Id.
	 * @return the skill Id
	 */
	public int getSkillId()
	{
		return _skillId;
	}
	
	/**
	 * Get the effect's skill level.
	 * @return the skill level
	 */
	public int getSkillLevel()
	{
		return _skillLevel;
	}
	
	/**
	 * Get the effect's duration.
	 * @return the duration in <b>seconds</b>
	 */
	public int getDuration()
	{
		return _duration;
	}
}