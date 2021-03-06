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
import org.lineage.gameserver.network.OutgoingPackets;

/**
 * update 27.8.10
 * @author kerberos, JIV
 */
public class ExStopMoveInAirShip implements IClientOutgoingPacket
{
	private final PlayerInstance _player;
	private final int _shipObjId;
	private final int x;
	private final int y;
	private final int z;
	private final int h;
	
	public ExStopMoveInAirShip(PlayerInstance player, int shipObjId)
	{
		_player = player;
		_shipObjId = shipObjId;
		x = player.getInVehiclePosition().getX();
		y = player.getInVehiclePosition().getY();
		z = player.getInVehiclePosition().getZ();
		h = player.getHeading();
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_STOP_MOVE_IN_AIRSHIP.writeId(packet);
		packet.writeD(_player.getObjectId());
		packet.writeD(_shipObjId);
		packet.writeD(x);
		packet.writeD(y);
		packet.writeD(z);
		packet.writeD(h);
		return true;
	}
}
