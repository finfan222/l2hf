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

import org.lineage.commons.database.DatabaseFactory;
import org.lineage.commons.network.PacketWriter;
import org.lineage.gameserver.data.sql.impl.CharNameTable;
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.OutgoingPackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Support for "Chat with Friends" dialog. <br />
 * This packet is sent only at login.
 * @author mrTJO, UnAfraid
 */
public class FriendListExtended implements IClientOutgoingPacket
{
	private final List<FriendInfo> _info;
	
	private static class FriendInfo
	{
		int _objId;
		String _name;
		boolean _online;
		int _classid;
		int _level;
		
		public FriendInfo(int objId, String name, boolean online, int classid, int level)
		{
			_objId = objId;
			_name = name;
			_online = online;
			_classid = classid;
			_level = level;
		}
	}
	
	public FriendListExtended(PlayerInstance player)
	{
		_info = new ArrayList<>(player.getFriendList().size());
		for (int objId : player.getFriendList())
		{
			final String name = CharNameTable.getInstance().getNameById(objId);
			final PlayerInstance player1 = World.getInstance().getPlayer(objId);
			boolean online = false;
			int classid = 0;
			int level = 0;
			if (player1 == null)
			{
				try (Connection con = DatabaseFactory.getConnection();
					PreparedStatement statement = con.prepareStatement("SELECT char_name, online, classid, level FROM characters WHERE charId = ?"))
				{
					statement.setInt(1, objId);
					try (ResultSet rset = statement.executeQuery())
					{
						if (rset.next())
						{
							_info.add(new FriendInfo(objId, rset.getString(1), rset.getInt(2) == 1, rset.getInt(3), rset.getInt(4)));
						}
					}
				}
				catch (Exception e)
				{
					// Who cares?
				}
				continue;
			}
			
			if (player1.isOnline())
			{
				online = true;
			}
			
			classid = player1.getClassId().getId();
			level = player1.getLevel();
			_info.add(new FriendInfo(objId, name, online, classid, level));
		}
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.FRIEND_LIST.writeId(packet);
		packet.writeD(_info.size());
		for (FriendInfo info : _info)
		{
			packet.writeD(info._objId); // character id
			packet.writeS(info._name);
			packet.writeD(info._online ? 0x01 : 0x00); // online
			packet.writeD(info._online ? info._objId : 0x00); // object id if online
			packet.writeD(info._classid);
			packet.writeD(info._level);
		}
		return true;
	}
}
