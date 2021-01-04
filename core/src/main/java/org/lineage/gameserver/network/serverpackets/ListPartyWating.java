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
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.partymatching.PartyMatchRoom;
import org.lineage.gameserver.model.partymatching.PartyMatchRoomList;
import org.lineage.gameserver.network.OutgoingPackets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gnacik
 */
public class ListPartyWating implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private final int _loc;
	private final int _lim;
	private final List<PartyMatchRoom> _rooms;
	
	public ListPartyWating(PlayerInstance player, int auto, int location, int limit)
	{
		_player = player;
		_loc = location;
		_lim = limit;
		_rooms = new ArrayList<>();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		for (PartyMatchRoom room : PartyMatchRoomList.getInstance().getRooms())
		{
			if ((room.getMembers() < 1) || (room.getOwner() == null) || !room.getOwner().isOnline() || (room.getOwner().getPartyRoom() != room.getId()))
			{
				PartyMatchRoomList.getInstance().deleteRoom(room.getId());
				continue;
			}
			if ((_loc > 0) && (_loc != room.getLocation()))
			{
				continue;
			}
			if ((_lim == 0) && ((_player.getLevel() < room.getMinLevel()) || (_player.getLevel() > room.getMaxLevel())))
			{
				continue;
			}
			_rooms.add(room);
		}
		final int size = _rooms.size();
		OutgoingPackets.LIST_PARTY_WAITING.writeId(packet);
		if (size > 0)
		{
			packet.writeD(0x01);
		}
		else
		{
			packet.writeD(0x00);
		}
		
		packet.writeD(_rooms.size());
		for (PartyMatchRoom room : _rooms)
		{
			packet.writeD(room.getId());
			packet.writeS(room.getTitle());
			packet.writeD(room.getLocation());
			packet.writeD(room.getMinLevel());
			packet.writeD(room.getMaxLevel());
			packet.writeD(room.getMaxMembers());
			packet.writeS(room.getOwner().getName());
			packet.writeD(room.getMembers());
			for (PlayerInstance member : room.getPartyMembers())
			{
				if (member != null)
				{
					packet.writeD(member.getClassId().getId());
					packet.writeS(member.getName());
				}
				else
				{
					packet.writeD(0x00);
					packet.writeS("Not Found");
				}
			}
		}
		return true;
	}
}
