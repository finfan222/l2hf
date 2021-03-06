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
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.ActionFailed;

import static org.lineage.gameserver.model.actor.Npc.INTERACTION_DISTANCE;

/**
 * @version $Revision: 1.2.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestPrivateStoreSell implements IClientIncomingPacket
{
	private static final int BATCH_LENGTH = 28; // length of the one item
	
	private int _storePlayerId;
	private ItemRequest[] _items = null;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_storePlayerId = packet.readD();
		final int count = packet.readD();
		if ((count <= 0) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != packet.getReadableBytes()))
		{
			return false;
		}
		_items = new ItemRequest[count];
		for (int i = 0; i < count; i++)
		{
			final int objectId = packet.readD();
			final int itemId = packet.readD();
			packet.readH(); // TODO analyse this
			packet.readH(); // TODO analyse this
			final long cnt = packet.readQ();
			final long price = packet.readQ();
			if ((objectId < 1) || (itemId < 1) || (cnt < 1) || (price < 0))
			{
				_items = null;
				return false;
			}
			_items[i] = new ItemRequest(objectId, itemId, cnt, price);
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
		
		if (!client.getFloodProtectors().getTransaction().tryPerformAction("privatestoresell"))
		{
			player.sendMessage("You are selling items too fast.");
			return;
		}
		
		final PlayerInstance object = World.getInstance().getPlayer(_storePlayerId);
		if (object == null)
		{
			return;
		}
		
		final PlayerInstance storePlayer = object;
		if (!player.isInsideRadius3D(storePlayer, INTERACTION_DISTANCE))
		{
			return;
		}
		
		if ((player.getInstanceId() != storePlayer.getInstanceId()) && (player.getInstanceId() != -1))
		{
			return;
		}
		
		if (storePlayer.getPrivateStoreType() != PrivateStoreType.BUY)
		{
			return;
		}
		
		if (player.isCursedWeaponEquipped())
		{
			return;
		}
		
		final TradeList storeList = storePlayer.getBuyList();
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
		
		if (!storeList.privateStoreSell(player, _items))
		{
			client.sendPacket(ActionFailed.STATIC_PACKET);
			LOGGER.warning("PrivateStore sell has failed due to invalid list or request. Player: " + player.getName() + ", Private store of: " + storePlayer.getName());
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
