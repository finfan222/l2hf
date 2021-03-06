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

import org.lineage.Config;
import org.lineage.commons.network.PacketReader;
import org.lineage.gameserver.handler.CommunityBoardHandler;
import org.lineage.gameserver.network.GameClient;

/**
 * RequestShowBoard client packet implementation.
 * @author Zoey76
 */
public class RequestShowBoard implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_unknown = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		CommunityBoardHandler.getInstance().handleParseCommand(Config.BBS_DEFAULT, client.getPlayer());
	}
}
