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
import org.lineage.commons.concurrent.ThreadPool;
import org.lineage.commons.network.PacketReader;
import org.lineage.gameserver.data.xml.impl.BuyListData;
import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.Npc;
import org.lineage.gameserver.model.actor.instance.MerchantInstance;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.buylist.BuyListHolder;
import org.lineage.gameserver.model.buylist.Product;
import org.lineage.gameserver.model.itemcontainer.Inventory;
import org.lineage.gameserver.model.items.Armor;
import org.lineage.gameserver.model.items.Item;
import org.lineage.gameserver.model.items.Weapon;
import org.lineage.gameserver.model.items.type.ArmorType;
import org.lineage.gameserver.model.items.type.WeaponType;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ActionFailed;
import org.lineage.gameserver.network.serverpackets.ShopPreviewInfo;
import org.lineage.gameserver.network.serverpackets.UserInfo;
import org.lineage.gameserver.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 ** @author Gnacik
 */
public class RequestPreviewItem implements IClientIncomingPacket
{
	@SuppressWarnings("unused")
	private int _unk;
	private int _listId;
	private int _count;
	private int[] _items;
	
	private class RemoveWearItemsTask implements Runnable
	{
		private final PlayerInstance _player;
		
		protected RemoveWearItemsTask(PlayerInstance player)
		{
			_player = player;
		}
		
		@Override
		public void run()
		{
			try
			{
				_player.sendPacket(SystemMessageId.YOU_ARE_NO_LONGER_TRYING_ON_EQUIPMENT_2);
				_player.sendPacket(new UserInfo(_player));
			}
			catch (Exception e)
			{
				LOGGER.log(Level.SEVERE, "", e);
			}
		}
	}
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_unk = packet.readD();
		_listId = packet.readD();
		_count = packet.readD();
		if (_count < 0)
		{
			_count = 0;
		}
		if (_count > 100)
		{
			return false; // prevent too long lists
		}
		
		// Create _items table that will contain all ItemID to Wear
		_items = new int[_count];
		
		// Fill _items table with all ItemID to Wear
		for (int i = 0; i < _count; i++)
		{
			_items[i] = packet.readD();
		}
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		if (_items == null)
		{
			return;
		}
		
		// Get the current player and return if null
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		if (!client.getFloodProtectors().getTransaction().tryPerformAction("buy"))
		{
			player.sendMessage("You are buying too fast.");
			return;
		}
		
		// If Alternate rule Karma punishment is set to true, forbid Wear to player with Karma
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() > 0))
		{
			return;
		}
		
		// Check current target of the player and the INTERACTION_DISTANCE
		final WorldObject target = player.getTarget();
		if (!player.isGM() && ((target == null // No target (i.e. GM Shop)
		) || !((target instanceof MerchantInstance)) // Target not a merchant
			|| !player.isInsideRadius2D(target, Npc.INTERACTION_DISTANCE) // Distance is too far
		))
		{
			return;
		}
		
		if ((_count < 1) || (_listId >= 4000000))
		{
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Get the current merchant targeted by the player
		final MerchantInstance merchant = (target instanceof MerchantInstance) ? (MerchantInstance) target : null;
		if (merchant == null)
		{
			LOGGER.warning(getClass().getName() + " Null merchant!");
			return;
		}
		
		final BuyListHolder buyList = BuyListData.getInstance().getBuyList(_listId);
		if (buyList == null)
		{
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId, Config.DEFAULT_PUNISH);
			return;
		}
		
		long totalPrice = 0;
		final Map<Integer, Integer> itemList = new HashMap<>();
		for (int i = 0; i < _count; i++)
		{
			final int itemId = _items[i];
			final Product product = buyList.getProductByItemId(itemId);
			if (product == null)
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId + " and item_id " + itemId, Config.DEFAULT_PUNISH);
				return;
			}
			
			final Item template = product.getItem();
			if (template == null)
			{
				continue;
			}
			
			final int slot = Inventory.getPaperdollIndex(template.getBodyPart());
			if (slot < 0)
			{
				continue;
			}
			
			if (template instanceof Weapon)
			{
				if (player.getRace().ordinal() == 5)
				{
					if (template.getItemType() == WeaponType.NONE)
					{
						continue;
					}
					else if ((template.getItemType() == WeaponType.RAPIER) || (template.getItemType() == WeaponType.CROSSBOW) || (template.getItemType() == WeaponType.ANCIENTSWORD))
					{
						continue;
					}
				}
			}
			else if (template instanceof Armor)
			{
				if ((player.getRace().ordinal() == 5) && ((template.getItemType() == ArmorType.HEAVY) || (template.getItemType() == ArmorType.MAGIC)))
				{
					continue;
				}
			}
			
			if (itemList.containsKey(slot))
			{
				player.sendPacket(SystemMessageId.YOU_CAN_NOT_TRY_THOSE_ITEMS_ON_AT_THE_SAME_TIME);
				return;
			}
			
			itemList.put(slot, itemId);
			totalPrice += Config.WEAR_PRICE;
			if (totalPrice > Inventory.MAX_ADENA)
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + Inventory.MAX_ADENA + " adena worth of goods.", Config.DEFAULT_PUNISH);
				return;
			}
		}
		
		// Charge buyer and add tax to castle treasury if not owned by npc clan because a Try On is not Free
		if ((totalPrice < 0) || !player.reduceAdena("Wear", totalPrice, player.getLastFolkNPC(), true))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		
		if (!itemList.isEmpty())
		{
			player.sendPacket(new ShopPreviewInfo(itemList));
			// Schedule task
			ThreadPool.schedule(new RemoveWearItemsTask(player), Config.WEAR_DELAY * 1000);
		}
	}
}