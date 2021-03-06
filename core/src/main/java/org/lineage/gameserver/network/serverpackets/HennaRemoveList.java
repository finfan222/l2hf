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
import org.lineage.gameserver.model.items.Henna;
import org.lineage.gameserver.network.OutgoingPackets;

/**
 * @author Zoey76
 */
public class HennaRemoveList implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	
	public HennaRemoveList(PlayerInstance player)
	{
		_player = player;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.HENNA_UNEQUIP_LIST.writeId(packet);
		packet.writeQ(_player.getAdena());
		packet.writeD(0x00);
		packet.writeD(3 - _player.getHennaEmptySlots());
		
		for (Henna henna : _player.getHennaList())
		{
			if (henna != null)
			{
				packet.writeD(henna.getDyeId());
				packet.writeD(henna.getDyeItemId());
				packet.writeD(henna.getCancelCount());
				packet.writeD(0x00);
				packet.writeD(henna.getCancelFee());
				packet.writeD(0x00);
				packet.writeD(0x01);
			}
		}
		return true;
	}
}
