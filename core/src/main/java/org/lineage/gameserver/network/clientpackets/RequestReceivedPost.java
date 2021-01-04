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
import org.lineage.gameserver.instancemanager.MailManager;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.entity.Message;
import org.lineage.gameserver.model.zone.ZoneId;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ExChangePostState;
import org.lineage.gameserver.network.serverpackets.ExReplyReceivedPost;
import org.lineage.gameserver.util.Util;

/**
 * @author Migi, DS
 */
public class RequestReceivedPost implements IClientIncomingPacket
{
	private int _msgId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_msgId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((player == null) || !Config.ALLOW_MAIL)
		{
			return;
		}
		
		final Message msg = MailManager.getInstance().getMessage(_msgId);
		if (msg == null)
		{
			return;
		}
		
		if (!player.isInsideZone(ZoneId.PEACE) && msg.hasAttachments())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_RECEIVE_OR_SEND_MAIL_WITH_ATTACHED_ITEMS_IN_NON_PEACE_ZONE_REGIONS);
			return;
		}
		
		if (msg.getReceiverId() != player.getObjectId())
		{
			Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to receive not own post!", Config.DEFAULT_PUNISH);
			return;
		}
		
		if (msg.isDeletedByReceiver())
		{
			return;
		}
		
		player.sendPacket(new ExReplyReceivedPost(msg));
		player.sendPacket(new ExChangePostState(true, _msgId, Message.READED));
		msg.markAsRead();
	}
}
