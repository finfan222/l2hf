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
import org.lineage.gameserver.data.xml.impl.EnchantItemData;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.items.enchant.EnchantScroll;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ExPutEnchantTargetItemResult;

import java.util.logging.Level;

/**
 * @author KenM
 */
public class RequestExTryToPutEnchantTargetItem implements IClientIncomingPacket
{
	private int _objectId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_objectId = packet.readD();
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		final PlayerInstance player = client.getPlayer();
		if ((_objectId == 0) || (player == null))
		{
			return;
		}
		
		if (player.isEnchanting())
		{
			return;
		}
		
		final ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		final ItemInstance scroll = player.getInventory().getItemByObjectId(player.getActiveEnchantItemId());
		if ((item == null) || (scroll == null))
		{
			return;
		}
		
		final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
		if ((scrollTemplate == null) || !scrollTemplate.isValid(item, null))
		{
			player.sendPacket(SystemMessageId.DOES_NOT_FIT_STRENGTHENING_CONDITIONS_OF_THE_SCROLL);
			player.setActiveEnchantItemId(PlayerInstance.ID_NONE);
			player.sendPacket(new ExPutEnchantTargetItemResult(0));
			if (scrollTemplate == null)
			{
				LOGGER.log(Level.WARNING, getClass().getSimpleName() + ": Undefined scroll have been used id: " + scroll.getId());
			}
			return;
		}
		player.setEnchanting(true);
		player.setActiveEnchantTimestamp(System.currentTimeMillis());
		player.sendPacket(new ExPutEnchantTargetItemResult(_objectId));
	}
}
