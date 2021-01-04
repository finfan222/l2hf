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

import org.lineage.gameserver.instancemanager.CastleManager;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.entity.Castle;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.SystemMessage;
import org.lineage.gameserver.util.Util;

/**
 * Player Can Take Castle condition implementation.
 * @author Adry_85
 */
public class ConditionPlayerCanTakeCastle extends Condition
{
	@Override
	public boolean testImpl(Creature effector, Creature effected, Skill skill, Item item)
	{
		if ((effector == null) || !effector.isPlayer())
		{
			return false;
		}
		
		final PlayerInstance player = effector.getActingPlayer();
		if (player.isAlikeDead() || player.isCursedWeaponEquipped() || !player.isClanLeader())
		{
			return false;
		}
		
		final Castle castle = CastleManager.getInstance().getCastle(player);
		SystemMessage sm;
		if ((castle == null) || (castle.getResidenceId() <= 0) || !castle.getSiege().isInProgress() || (castle.getSiege().getAttackerClan(player.getClan()) == null))
		{
			sm = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
			sm.addSkillName(skill);
			player.sendPacket(sm);
			return false;
		}
		else if (!castle.getArtefacts().contains(effected))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return false;
		}
		else if (!Util.checkIfInRange(skill.getCastRange(), player, effected, true) || (Math.abs(player.getZ() - effected.getZ()) > 40))
		{
			player.sendPacket(SystemMessageId.THE_DISTANCE_IS_TOO_FAR_AND_SO_THE_CASTING_HAS_BEEN_STOPPED);
			return false;
		}
		castle.getSiege().announceToPlayer(new SystemMessage(SystemMessageId.THE_OPPOSING_CLAN_HAS_STARTED_TO_ENGRAVE_THE_HOLY_ARTIFACT), false);
		return true;
	}
}