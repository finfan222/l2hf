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
import org.lineage.gameserver.network.OutgoingPackets;

/**
 * Duel End packet implementation.
 * @author KenM, Zoey76
 */
public class ExDuelEnd implements IClientOutgoingPacket
{
	public static final ExDuelEnd PLAYER_DUEL = new ExDuelEnd(false);
	public static final ExDuelEnd PARTY_DUEL = new ExDuelEnd(true);
	
	private final int _partyDuel;
	
	private ExDuelEnd(boolean isPartyDuel)
	{
		_partyDuel = isPartyDuel ? 1 : 0;
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.EX_DUEL_END.writeId(packet);
		packet.writeD(_partyDuel);
		return true;
	}
}
