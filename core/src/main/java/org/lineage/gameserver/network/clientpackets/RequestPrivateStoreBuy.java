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
import org.lineage.gameserver.data.sql.impl.OfflineTraderTable;
import org.lineage.gameserver.enums.PrivateStoreType;
import org.lineage.gameserver.model.ItemRequest;
import org.lineage.gameserver.model.TradeList;
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.ActionFailed;
import org.lineage.gameserver.util.Util;

import java.util.HashSet;
import java.util.Set;

import static org.lineage.gameserver.model.actor.Npc.INTERACTION_DISTANCE;

public class RequestPrivateStoreBuy implements IClientIncomingPacket
{
	private static final int BATCH_LENGTH = 20; // length of the one item
	
	private int _storePlayerId;
	private Set<ItemRequest> _items = null;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_storePlayerId = packet.readD();
		final int count = packet.readD();
		if ((count <= 0) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != packet.getReadableBytes()))
		{
			return false;
		}
		_items = new HashSet<>();
		for (int i = 0; i < count; i++)
		{
			final int objectId = packet.readD();
			final long cnt = packet.readQ();
			final long price = packet.readQ();
			if ((objectId < 1) || (cnt < 1) || (price < 0))
			{
				_items = null;
				return false;
			}
			
			_items.add(new ItemRequest(objectId, cnt, price));
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
		
		if (_items == null)
		{
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (!client.getFloodProtectors().getTransaction().tryPerformAction("privatestorebuy"))
		{
			player.sendMessage("You are buying items too fast.");
			return;
		}
		
		final WorldObject object = World.getInstance().getPlayer(_storePlayerId);
		if (object == null)
		{
			return;
		}
		
		if (player.isCursedWeaponEquipped())
		{
			return;
		}
		
		final PlayerInstance storePlayer = (PlayerInstance) object;
		if (!player.isInsideRadius3D(storePlayer, INTERACTION_DISTANCE))
		{
			return;
		}
		
		if ((player.getInstanceId() != storePlayer.getInstanceId()) && (player.getInstanceId() != -1))
		{
			return;
		}
		
		if (!((storePlayer.getPrivateStoreType() == PrivateStoreType.SELL) || (storePlayer.getPrivateStoreType() == PrivateStoreType.PACKAGE_SELL)))
		{
			return;
		}
		
		final TradeList storeList = storePlayer.getSellList();
		if (storeList == null)
		{
			return;
		}
		
		if (!player.getAccessLevel().allowTransaction())
		{
			player.sendMessage("Transactions are disabled for your Access Level.");
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if ((storePlayer.getPrivateStoreType() == PrivateStoreType.PACKAGE_SELL) && (storeList.getItemCount() > _items.size()))
		{
			final String msgErr = "[RequestPrivateStoreBuy] player " + client.getPlayer().getName() + " tried to buy less items than sold by package-sell, ban this player for bot usage!";
			Util.handleIllegalPlayerAction(client.getPlayer(), msgErr, Config.DEFAULT_PUNISH);
			return;
		}
		
		final int result = storeList.privateStoreBuy(player, _items);
		if (result > 0)
		{
			client.sendPacket(ActionFailed.STATIC_PACKET);
			if (result > 1)
			{
				LOGGER.warning("PrivateStore buy has failed due to invalid list or request. Player: " + player.getName() + ", Private store of: " + storePlayer.getName());
			}
			return;
		}
		
		// Update offline trade record, if realtime saving is enabled
		if (Config.OFFLINE_TRADE_ENABLE && Config.STORE_OFFLINE_TRADE_IN_REALTIME && ((storePlayer.getClient() == null) || storePlayer.getClient().isDetached()))
		{
			OfflineTraderTable.onTransaction(storePlayer, storeList.getItemCount() == 0, false);
		}
		
		if (storeList.getItemCount() == 0)
		{
			storePlayer.setPrivateStoreType(PrivateStoreType.NONE);
			storePlayer.broadcastUserInfo();
		}
	}
}
