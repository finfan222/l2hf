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
import org.lineage.gameserver.model.Party;
import org.lineage.gameserver.model.Party.MessageType;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.partymatching.PartyMatchRoom;
import org.lineage.gameserver.model.partymatching.PartyMatchRoomList;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.ExClosePartyRoom;
import org.lineage.gameserver.network.serverpackets.ExPartyRoomMember;
import org.lineage.gameserver.network.serverpackets.PartyMatchDetail;

/**
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestWithDrawalParty implements IClientIncomingPacket
{
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
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
		
		final Party party = player.getParty();
		if (party != null)
		{
			if (party.isInDimensionalRift() && !party.getDimensionalRift().getRevivedAtWaitingRoom().contains(player))
			{
				player.sendMessage("You can't exit party when you are in Dimensional Rift.");
			}
			else
			{
				party.removePartyMember(player, MessageType.LEFT);
				if (player.isInPartyMatchRoom())
				{
					final PartyMatchRoom room = PartyMatchRoomList.getInstance().getPlayerRoom(player);
					if (room != null)
					{
						player.sendPacket(new PartyMatchDetail(room));
						player.sendPacket(new ExPartyRoomMember(room, 0));
						player.sendPacket(new ExClosePartyRoom());
						room.deleteMember(player);
					}
					player.setPartyRoom(0);
					// player.setPartyMatching(0);
					player.broadcastUserInfo();
				}
			}
		}
	}
}
