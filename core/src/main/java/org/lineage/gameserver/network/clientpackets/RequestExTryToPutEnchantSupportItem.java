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
import org.lineage.gameserver.model.items.enchant.EnchantSupportItem;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ExPutEnchantSupportItemResult;

/**
 * @author KenM
 */
public class RequestExTryToPutEnchantSupportItem implements IClientIncomingPacket
{
	private int _supportObjectId;
	private int _enchantObjectId;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_supportObjectId = packet.readD();
		_enchantObjectId = packet.readD();
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
		
		if (player.isEnchanting())
		{
			final ItemInstance item = player.getInventory().getItemByObjectId(_enchantObjectId);
			final ItemInstance scroll = player.getInventory().getItemByObjectId(player.getActiveEnchantItemId());
			final ItemInstance support = player.getInventory().getItemByObjectId(_supportObjectId);
			if ((item == null) || (scroll == null) || (support == null))
			{
				// message may be custom
				player.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS);
				player.setActiveEnchantSupportItemId(PlayerInstance.ID_NONE);
				return;
			}
			
			final EnchantScroll scrollTemplate = EnchantItemData.getInstance().getEnchantScroll(scroll);
			final EnchantSupportItem supportTemplate = EnchantItemData.getInstance().getSupportItem(support);
			if ((scrollTemplate == null) || (supportTemplate == null) || !scrollTemplate.isValid(item, supportTemplate))
			{
				// message may be custom
				player.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITIONS);
				player.setActiveEnchantSupportItemId(PlayerInstance.ID_NONE);
				player.sendPacket(new ExPutEnchantSupportItemResult(0));
				return;
			}
			player.setActiveEnchantSupportItemId(support.getObjectId());
			player.sendPacket(new ExPutEnchantSupportItemResult(_supportObjectId));
		}
	}
}
