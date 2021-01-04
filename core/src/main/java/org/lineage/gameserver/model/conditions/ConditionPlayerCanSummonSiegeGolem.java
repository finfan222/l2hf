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

import org.lineage.gameserver.SevenSigns;
import org.lineage.gameserver.instancemanager.CastleManager;
import org.lineage.gameserver.instancemanager.FortManager;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.entity.Castle;
import org.lineage.gameserver.model.entity.Fort;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.network.SystemMessageId;

/**
 * Player Can Summon Siege Golem implementation.
 * @author Adry_85
 */
public class ConditionPlayerCanSummonSiegeGolem extends Condition
{
	private final boolean _value;
	
	public ConditionPlayerCanSummonSiegeGolem(boolean value)
	{
		_value = value;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effector == null) || !effector.isPlayer())
		{
			return !_value;
		}
		
		final PlayerInstance player = effector.getActingPlayer();
		boolean canSummonSiegeGolem = !player.isAlikeDead() && !player.isCursedWeaponEquipped() && (player.getClan() != null);
		final Castle castle = CastleManager.getInstance().getCastle(player);
		final Fort fort = FortManager.getInstance().getFort(player);
		if ((castle == null) && (fort == null))
		{
			canSummonSiegeGolem = false;
		}
		
		if (((fort != null) && (fort.getResidenceId() == 0)) || ((castle != null) && (castle.getResidenceId() == 0)))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			canSummonSiegeGolem = false;
		}
		else if (((castle != null) && !castle.getSiege().isInProgress()) || ((fort != null) && !fort.getSiege().isInProgress()))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			canSummonSiegeGolem = false;
		}
		else if ((player.getClanId() != 0) && (((castle != null) && (castle.getSiege().getAttackerClan(player.getClanId()) == null)) || ((fort != null) && (fort.getSiege().getAttackerClan(player.getClanId()) == null))))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			canSummonSiegeGolem = false;
		}
		else if ((SevenSigns.getInstance().checkSummonConditions(player)))
		{
			canSummonSiegeGolem = false;
		}
		return _value == canSummonSiegeGolem;
	}
}