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
import org.lineage.gameserver.handler.BypassHandler;
import org.lineage.gameserver.handler.IBypassHandler;
import org.lineage.gameserver.model.actor.instance.ClassMasterInstance;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.quest.QuestState;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.TutorialCloseHtml;

public class RequestTutorialLinkHtml implements IClientIncomingPacket
{
	private String _bypass;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_bypass = packet.readS();
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
		
		if (_bypass.equalsIgnoreCase("close"))
		{
			player.sendPacket(TutorialCloseHtml.STATIC_PACKET);
		}
		
		final IBypassHandler handler = BypassHandler.getInstance().getHandler(_bypass);
		if (handler != null)
		{
			handler.useBypass(_bypass, player, null);
		}
		else
		{
			ClassMasterInstance.onTutorialLink(player, _bypass);
			
			final QuestState qs = player.getQuestState("Q00255_Tutorial");
			if (qs != null)
			{
				qs.getQuest().notifyEvent(_bypass, null, player);
			}
		}
	}
}
