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
import org.lineage.gameserver.model.PlayerCondOverride;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.partymatching.PartyMatchRoom;
import org.lineage.gameserver.model.partymatching.PartyMatchRoomList;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.CreatureSay;

/**
 * Party Match Room chat handler.
 * @author Gnacik
 */
public class ChatPartyMatchRoom implements IChatHandler
{
	private static final ChatType[] CHAT_TYPES =
	{
		ChatType.PARTYMATCH_ROOM,
	};
	
	@Override
	public void handleChat(ChatType type, PlayerInstance activeChar, String target, String text)
	{
		if (!activeChar.isInPartyMatchRoom())
		{
			return;
		}
		final PartyMatchRoom room = PartyMatchRoomList.getInstance().getPlayerRoom(activeChar);
		if (room == null)
		{
			return;
		}
		if (activeChar.isChatBanned() && Config.BAN_CHAT_CHANNELS.contains(type))
		{
			activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED_IF_YOU_TRY_TO_CHAT_BEFORE_THE_PROHIBITION_IS_REMOVED_THE_PROHIBITION_TIME_WILL_INCREASE_EVEN_FURTHER);
			return;
		}
		if (Config.JAIL_DISABLE_CHAT && activeChar.isJailed() && !activeChar.canOverrideCond(PlayerCondOverride.CHAT_CONDITIONS))
		{
			activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
			return;
		}
		
		final CreatureSay cs = new CreatureSay(activeChar, type, activeChar.getName(), text);
		for (PlayerInstance member : room.getPartyMembers())
		{
			if (Config.FACTION_SYSTEM_ENABLED)
			{
				if (Config.FACTION_SPECIFIC_CHAT)
				{
					if ((activeChar.isGood() && member.isGood()) || (activeChar.isEvil() && member.isEvil()))
					{
						member.sendPacket(cs);
					}
				}
				else
				{
					member.sendPacket(cs);
				}
			}
			else
			{
				member.sendPacket(cs);
			}
		}
	}
	
	@Override
	public ChatType[] getChatTypeList()
	{
		return CHAT_TYPES;
	}
}