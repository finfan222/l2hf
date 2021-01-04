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
import org.lineage.gameserver.SevenSigns;
import org.lineage.gameserver.network.OutgoingPackets;

public class ShowMiniMap implements IClientOutgoingPacket
{
	private final int _mapId;
	
	/**
	 * @param mapId
	 */
	public ShowMiniMap(int mapId)
	{
		_mapId = mapId;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.SHOW_MINIMAP.writeId(packet);
		packet.writeD(_mapId);
		packet.writeC(SevenSigns.getInstance().getCurrentPeriod());
		return true;
	}
}
