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
import org.lineage.gameserver.model.clan.Clan;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.skills.Skill;

import java.util.List;

/**
 * The Class ConditionPlayerHasClanHall.
 * @author MrPoke
 */
public class ConditionPlayerHasClanHall extends Condition
{
	private final List<Integer> _clanHall;
	
	/**
	 * Instantiates a new condition player has clan hall.
	 * @param clanHall the clan hall
	 */
	public ConditionPlayerHasClanHall(List<Integer> clanHall)
	{
		_clanHall = clanHall;
	}
	
	/**
	 * Test impl.
	 * @return true, if successful
	 */
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if (effector.getActingPlayer() == null)
		{
			return false;
		}
		
		final Clan clan = effector.getActingPlayer().getClan();
		if (clan == null)
		{
			return (_clanHall.size() == 1) && (_clanHall.get(0) == 0);
		}
		
		// All Clan Hall
		if ((_clanHall.size() == 1) && (_clanHall.get(0) == -1))
		{
			return clan.getHideoutId() > 0;
		}
		return _clanHall.contains(clan.getHideoutId());
	}
}
