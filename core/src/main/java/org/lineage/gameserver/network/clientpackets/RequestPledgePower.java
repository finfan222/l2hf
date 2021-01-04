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
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.clan.ClanPrivilege;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.ManagePledgePower;

public class RequestPledgePower implements IClientIncomingPacket
{
	private int _rank;
	private int _action;
	private int _privs;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_rank = packet.readD();
		_action = packet.readD();
		if (_action == 2)
		{
			_privs = packet.readD();
		}
		else
		{
			_privs = 0;
		}
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
		
		if (_action == 2)
		{
			if (player.isClanLeader())
			{
				if (_rank == 9)
				{
					// The rights below cannot be bestowed upon Academy members:
					// Join a clan or be dismissed
					// Title management, crest management, master management, level management,
					// bulletin board administration
					// Clan war, right to dismiss, set functions
					// Auction, manage taxes, attack/defend registration, mercenary management
					// => Leaves only CP_CL_VIEW_WAREHOUSE, CP_CH_OPEN_DOOR, CP_CS_OPEN_DOOR?
					_privs &= ClanPrivilege.CL_VIEW_WAREHOUSE.getBitmask() | ClanPrivilege.CH_OPEN_DOOR.getBitmask() | ClanPrivilege.CS_OPEN_DOOR.getBitmask();
				}
				player.getClan().setRankPrivs(_rank, _privs);
			}
		}
		else
		{
			player.sendPacket(new ManagePledgePower(client.getPlayer().getClan(), _action, _rank));
		}
	}
}