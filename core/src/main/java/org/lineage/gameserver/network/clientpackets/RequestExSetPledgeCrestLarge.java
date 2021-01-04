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
import org.lineage.gameserver.data.sql.impl.CrestTable;
import org.lineage.gameserver.model.Crest;
import org.lineage.gameserver.model.Crest.CrestType;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.clan.Clan;
import org.lineage.gameserver.model.clan.ClanPrivilege;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;

/**
 * Format : chdb c (id) 0xD0 h (subid) 0x11 d data size b raw data (picture i think ;) )
 * @author -Wooden-
 */
public class RequestExSetPledgeCrestLarge implements IClientIncomingPacket
{
	private int _length;
	private byte[] _data = null;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_length = packet.readD();
		if (_length > 2176)
		{
			return false;
		}
		
		_data = packet.readB(_length);
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
		
		final Clan clan = player.getClan();
		if (clan == null)
		{
			return;
		}
		
		if ((_length < 0) || (_length > 2176))
		{
			player.sendPacket(SystemMessageId.THE_SIZE_OF_THE_UPLOADED_CREST_OR_INSIGNIA_DOES_NOT_MEET_THE_STANDARD_REQUIREMENTS);
			return;
		}
		
		if (clan.getDissolvingExpiryTime() > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.AS_YOU_ARE_CURRENTLY_SCHEDULE_FOR_CLAN_DISSOLUTION_YOU_CANNOT_REGISTER_OR_DELETE_A_CLAN_CREST);
			return;
		}
		
		if (!player.hasClanPrivilege(ClanPrivilege.CL_REGISTER_CREST))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		if (_length == 0)
		{
			if (clan.getCrestLargeId() != 0)
			{
				clan.changeLargeCrest(0);
				player.sendPacket(SystemMessageId.THE_CLAN_S_CREST_HAS_BEEN_DELETED);
			}
		}
		else
		{
			if (clan.getLevel() < 3)
			{
				player.sendPacket(SystemMessageId.A_CLAN_CREST_CAN_ONLY_BE_REGISTERED_WHEN_THE_CLAN_S_SKILL_LEVEL_IS_3_OR_ABOVE);
				return;
			}
			
			final Crest crest = CrestTable.getInstance().createCrest(_data, CrestType.PLEDGE_LARGE);
			if (crest != null)
			{
				clan.changeLargeCrest(crest.getId());
				player.sendPacket(SystemMessageId.THE_CLAN_CREST_WAS_SUCCESSFULLY_REGISTERED_REMEMBER_ONLY_A_CLAN_THAT_OWNS_A_CLAN_HALL_OR_CASTLE_CAN_DISPLAY_A_CREST);
			}
		}
	}
	
}
