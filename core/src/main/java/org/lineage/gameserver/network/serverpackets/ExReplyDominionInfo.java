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
package org.lineage.gameserver.network.serverpackets;

import org.lineage.commons.network.PacketWriter;
import org.lineage.gameserver.instancemanager.CastleManager;
import org.lineage.gameserver.instancemanager.TerritoryWarManager;
import org.lineage.gameserver.instancemanager.TerritoryWarManager.Territory;
import org.lineage.gameserver.network.OutgoingPackets;

import java.util.List;

/**
 * @author JIV
 */
public class ExReplyDominionInfo implements IClientOutgoingPacket
{
	public static final ExReplyDominionInfo STATIC_PACKET = new ExReplyDominionInfo();
	
	private ExReplyDominionInfo()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_REPLY_DOMINION_INFO.writeId(packet);
		final List<Territory> territoryList = TerritoryWarManager.getInstance().getAllTerritories();
		packet.writeD(territoryList.size()); // Territory Count
		for (Territory t : territoryList)
		{
			packet.writeD(t.getTerritoryId()); // Territory Id
			packet.writeS(CastleManager.getInstance().getCastleById(t.getCastleId()).getName().toLowerCase() + "_dominion"); // territory name
			packet.writeS(t.getOwnerClan().getName());
			packet.writeD(t.getOwnedWardIds().size()); // Emblem Count
			for (int i : t.getOwnedWardIds())
			{
				packet.writeD(i); // Emblem ID - should be in for loop for emblem count
			}
			packet.writeD((int) (TerritoryWarManager.getInstance().getTWStartTimeInMillis() / 1000));
		}
		return true;
	}
}
