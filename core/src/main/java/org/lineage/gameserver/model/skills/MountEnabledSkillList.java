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
package org.lineage.gameserver.model.skills;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mobius
 */
public class MountEnabledSkillList
{
	private static final List<Integer> ENABLED_SKILLS = new ArrayList<>(2);
	static
	{
		ENABLED_SKILLS.add(4289); // Wyvern Breath
		ENABLED_SKILLS.add(325); // Strider Siege Assault
	}
	
	public static boolean contains(int skillId)
	{
		return ENABLED_SKILLS.contains(skillId);
	}
}
