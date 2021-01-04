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
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.partymatching.PartyMatchRoom;
import org.lineage.gameserver.model.partymatching.PartyMatchRoomList;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ExManagePartyRoomMember;
import org.lineage.gameserver.network.serverpackets.JoinParty;
import org.lineage.gameserver.network.serverpackets.SystemMessage;

public class RequestAnswerJoinParty implements IClientIncomingPacket
{
	private int _response;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_response = packet.readD();
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
		
		final PlayerInstance requestor = player.getActiveRequester();
		if (requestor == null)
		{
			return;
		}
		
		requestor.sendPacket(new JoinParty(_response));
		
		switch (_response)
		{
			case -1: // Party disable by player client config
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.C1_IS_SET_TO_REFUSE_PARTY_REQUESTS_AND_CANNOT_RECEIVE_A_PARTY_REQUEST);
				sm.addPcName(player);
				requestor.sendPacket(sm);
				break;
			}
			case 0: // Party cancel by player
			{
				// requestor.sendPacket(SystemMessageId.PLAYER_DECLINED); FIXME: Done in client?
				break;
			}
			case 1: // Party accept by player
			{
				if (requestor.isInParty())
				{
					if (requestor.getParty().getMemberCount() >= 9)
					{
						final SystemMessage sm = new SystemMessage(SystemMessageId.THE_PARTY_IS_FULL);
						player.sendPacket(sm);
						requestor.sendPacket(sm);
						return;
					}
					player.joinParty(requestor.getParty());
				}
				else
				{
					requestor.setParty(new Party(requestor, requestor.getPartyDistributionType()));
					player.joinParty(requestor.getParty());
				}
				
				if (requestor.isInPartyMatchRoom() && player.isInPartyMatchRoom())
				{
					final PartyMatchRoomList list = PartyMatchRoomList.getInstance();
					if ((list != null) && (list.getPlayerRoomId(requestor) == list.getPlayerRoomId(player)))
					{
						final PartyMatchRoom room = list.getPlayerRoom(requestor);
						if (room != null)
						{
							final ExManagePartyRoomMember packet = new ExManagePartyRoomMember(player, room, 1);
							for (PlayerInstance member : room.getPartyMembers())
							{
								if (member != null)
								{
									member.sendPacket(packet);
								}
							}
						}
					}
				}
				else if (requestor.isInPartyMatchRoom() && !player.isInPartyMatchRoom())
				{
					final PartyMatchRoomList list = PartyMatchRoomList.getInstance();
					if (list != null)
					{
						final PartyMatchRoom room = list.getPlayerRoom(requestor);
						if (room != null)
						{
							room.addMember(player);
							final ExManagePartyRoomMember packet = new ExManagePartyRoomMember(player, room, 1);
							for (PlayerInstance member : room.getPartyMembers())
							{
								if (member != null)
								{
									member.sendPacket(packet);
								}
							}
							player.setPartyRoom(room.getId());
							// player.setPartyMatching(1);
							player.broadcastUserInfo();
						}
					}
				}
				break;
			}
		}
		
		if (requestor.isInParty())
		{
			requestor.getParty().setPendingInvitation(false); // if party is null, there is no need of decreasing
		}
		
		player.setActiveRequester(null);
		requestor.onTransactionResponse();
	}
}
