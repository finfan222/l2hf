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

import org.lineage.Config;
import org.lineage.commons.network.PacketWriter;
import org.lineage.gameserver.model.PlayerCondOverride;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.network.OutgoingPackets;

import java.util.Collection;

public class TradeStart extends AbstractItemPacket
{
	private final PlayerInstance _player;
	private final Collection<ItemInstance> _itemList;
	
	public TradeStart(PlayerInstance player)
	{
		_player = player;
		_itemList = _player.getInventory().getAvailableItems(true, (_player.canOverrideCond(PlayerCondOverride.ITEM_CONDITIONS) && Config.GM_TRADE_RESTRICTED_ITEMS), false);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		if ((_player.getActiveTradeList() == null) || (_player.getActiveTradeList().getPartner() == null))
		{
			return false;
		}
		
		OutgoingPackets.TRADE_START.writeId(packet);
		packet.writeD(_player.getActiveTradeList().getPartner().getObjectId());
		packet.writeH(_itemList.size());
		for (ItemInstance item : _itemList)
		{
			writeItem(packet, item);
		}
		return true;
	}
}
