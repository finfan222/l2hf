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
package org.lineage.gameserver.network.clientpackets;

import org.lineage.commons.network.PacketReader;
import org.lineage.gameserver.instancemanager.RaidBossPointsManager;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.ExGetBossRecord;

import java.util.Map;

/**
 * Format: (ch) d
 * @author -Wooden-
 */
public class RequestGetBossRecord implements IClientIncomingPacket
{
	private int _bossId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_bossId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (_bossId != 0)
		{
			LOGGER.info("C5: RequestGetBossRecord: d: " + _bossId + " ActiveChar: " + player); // should be always 0, log it if isnt 0 for furture research
		}
		
		final int points = RaidBossPointsManager.getInstance().getPointsByOwnerId(player.getObjectId());
		final int ranking = RaidBossPointsManager.getInstance().calculateRanking(player.getObjectId());
		final Map<Integer, Integer> list = RaidBossPointsManager.getInstance().getList(player);
		
		// trigger packet
		player.sendPacket(new ExGetBossRecord(ranking, points, list));
	}
}