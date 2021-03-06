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
import org.lineage.gameserver.model.Party;
import org.lineage.gameserver.network.OutgoingPackets;

/**
 * @author chris_00
 */
public class ExMPCCPartyInfoUpdate implements IClientOutgoingPacket
{
	private final int _mode;
	private final int _LeaderOID;
	private final int _memberCount;
	private final String _name;
	
	/**
	 * @param party
	 * @param mode 0 = Remove, 1 = Add
	 */
	public ExMPCCPartyInfoUpdate(Party party, int mode)
	{
		_name = party.getLeader().getName();
		_LeaderOID = party.getLeaderObjectId();
		_memberCount = party.getMemberCount();
		_mode = mode;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_MPCC_PARTY_INFO_UPDATE.writeId(packet);
		packet.writeS(_name);
		packet.writeD(_LeaderOID);
		packet.writeD(_memberCount);
		packet.writeD(_mode); // mode 0 = Remove Party, 1 = AddParty, maybe more...
		return true;
	}
}
