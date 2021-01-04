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
package handlers.chathandlers;

import org.lineage.Config;
import org.lineage.gameserver.enums.ChatType;
import org.lineage.gameserver.handler.IChatHandler;
import org.lineage.gameserver.instancemanager.TerritoryWarManager;
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.CreatureSay;

/**
 * Battlefield Chat Handler.
 * @author Gigiikun
 */
public class ChatBattlefield implements IChatHandler
{
	private static final ChatType[] CHAT_TYPES =
	{
		ChatType.BATTLEFIELD,
	};
	
	@Override
	public void handleChat(ChatType type, PlayerInstance activeChar, String target, String text)
	{
		if (TerritoryWarManager.getInstance().isTWChannelOpen() && (activeChar.getSiegeSide() > 0))
		{
			if (activeChar.isChatBanned() && Config.BAN_CHAT_CHANNELS.contains(type))
			{
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED_IF_YOU_TRY_TO_CHAT_BEFORE_THE_PROHIBITION_IS_REMOVED_THE_PROHIBITION_TIME_WILL_INCREASE_EVEN_FURTHER);
				return;
			}
			
			final CreatureSay cs = new CreatureSay(activeChar, type, activeChar.getName(), text);
			for (PlayerInstance player : World.getInstance().getPlayers())
			{
				if (player.getSiegeSide() == activeChar.getSiegeSide())
				{
					player.sendPacket(cs);
				}
			}
		}
	}
	
	@Override
	public ChatType[] getChatTypeList()
	{
		return CHAT_TYPES;
	}
}