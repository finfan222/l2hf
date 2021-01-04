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
package handlers.effecthandlers;

import org.lineage.gameserver.instancemanager.FortManager;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.clan.Clan;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.entity.Fort;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.SystemMessage;

/**
 * Take Fort Start effect implementation.
 * @author UnAfraid
 */
public class TakeFortStart extends AbstractEffect
{
	public TakeFortStart(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		if (info.getEffector().isPlayer())
		{
			final PlayerInstance player = info.getEffector().getActingPlayer();
			final Fort fort = FortManager.getInstance().getFort(player);
			final Clan clan = player.getClan();
			if ((fort != null) && (clan != null))
			{
				fort.getSiege().announceToPlayer(new SystemMessage(SystemMessageId.S1_CLAN_IS_TRYING_TO_DISPLAY_A_FLAG), clan.getName());
			}
		}
	}
}
