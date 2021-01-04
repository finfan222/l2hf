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

import org.lineage.gameserver.enums.InstanceType;
import org.lineage.gameserver.model.actor.Attackable;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.templates.NpcTemplate;

/**
 * This class represents Friendly Mobs lying over the world.<br>
 * These friendly mobs should only attack players with karma > 0 and it is always aggro, since it just attacks players with karma.
 */
public class FriendlyMobInstance extends Attackable
{
	/**
	 * Creates a friendly monster.
	 * @param template the friendly monster NPC template
	 */
	public FriendlyMobInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.FriendlyMobInstance);
	}
	
	@Override
	public boolean isAutoAttackable(Creature attacker)
	{
		if (attacker.isPlayer())
		{
			return ((PlayerInstance) attacker).getKarma() > 0;
		}
		return false;
	}
	
	@Override
	public boolean isAggressive()
	{
		return true;
	}
}
