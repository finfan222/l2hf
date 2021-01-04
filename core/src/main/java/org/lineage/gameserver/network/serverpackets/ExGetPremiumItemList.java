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
import org.lineage.gameserver.model.PremiumItem;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.OutgoingPackets;

import java.util.Map.Entry;

/**
 * @author Gnacik
 */
public class ExGetPremiumItemList implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public ExGetPremiumItemList(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_GET_PREMIUM_ITEM_LIST.writeId(packet);
		packet.writeD(_player.getPremiumItemList().size());
		for (Entry<Integer, PremiumItem> entry : _player.getPremiumItemList().entrySet())
		{
			final PremiumItem item = entry.getValue();
			packet.writeD(entry.getKey());
			packet.writeD(_player.getObjectId());
			packet.writeD(item.getItemId());
			packet.writeQ(item.getCount());
			packet.writeD(0x00); // ?
			packet.writeS(item.getSender());
		}
		return true;
	}
}
