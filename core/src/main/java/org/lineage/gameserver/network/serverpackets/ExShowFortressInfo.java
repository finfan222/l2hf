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
import org.lineage.gameserver.instancemanager.FortManager;
import org.lineage.gameserver.model.clan.Clan;
import org.lineage.gameserver.model.entity.Fort;
import org.lineage.gameserver.network.OutgoingPackets;

import java.util.List;

/**
 * @author KenM
 */
public class ExShowFortressInfo implements IClientOutgoingPacket
{
	public static final ExShowFortressInfo STATIC_PACKET = new ExShowFortressInfo();
	
	private ExShowFortressInfo()
	{
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_SHOW_FORTRESS_INFO.writeId(packet);
		final List<Fort> forts = FortManager.getInstance().getForts();
		packet.writeD(forts.size());
		for (Fort fort : forts)
		{
			final Clan clan = fort.getOwnerClan();
			packet.writeD(fort.getResidenceId());
			packet.writeS(clan != null ? clan.getName() : "");
			packet.writeD(fort.getSiege().isInProgress() ? 0x01 : 0x00);
			// Time of possession
			packet.writeD(fort.getOwnedTime());
		}
		return true;
	}
}