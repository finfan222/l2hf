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
import org.lineage.gameserver.data.xml.impl.RecipeData;
import org.lineage.gameserver.enums.PrivateStoreType;
import org.lineage.gameserver.model.ManufactureItem;
import org.lineage.gameserver.model.RecipeList;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.zone.ZoneId;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ActionFailed;
import org.lineage.gameserver.network.serverpackets.RecipeShopMsg;
import org.lineage.gameserver.taskmanager.AttackStanceTaskManager;
import org.lineage.gameserver.util.Broadcast;
import org.lineage.gameserver.util.Util;

import java.util.Arrays;
import java.util.List;

import static org.lineage.gameserver.model.itemcontainer.Inventory.MAX_ADENA;

/**
 * RequestRecipeShopListSet client packet class.
 */
public class RequestRecipeShopListSet implements IClientIncomingPacket
{
	private static final int BATCH_LENGTH = 12;
	
	private ManufactureItem[] _items = null;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		final int count = packet.readD();
		if ((count <= 0) || (count > Config.MAX_ITEM_IN_PACKET) || ((count * BATCH_LENGTH) != packet.getReadableBytes()))
		{
			return false;
		}
		
		_items = new ManufactureItem[count];
		for (int i = 0; i < count; i++)
		{
			final int id = packet.readD();
			final long cost = packet.readQ();
			if (cost < 0)
			{
				_items = null;
				return false;
			}
			_items[i] = new ManufactureItem(id, cost);
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
			player.setPrivateStoreType(PrivateStoreType.NONE);
			player.broadcastUserInfo();
			return;
		}
		
		if (AttackStanceTaskManager.getInstance().hasAttackStanceTask(player) || player.isInDuel())
		{
			client.sendPacket(SystemMessageId.WHILE_YOU_ARE_ENGAGED_IN_COMBAT_YOU_CANNOT_OPERATE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInsideZone(ZoneId.NO_STORE))
		{
			client.sendPacket(SystemMessageId.YOU_CANNOT_OPEN_A_PRIVATE_WORKSHOP_HERE);
			client.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (!player.canOpenPrivateStore())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final List<RecipeList> dwarfRecipes = Arrays.asList(player.getDwarvenRecipeBook());
		final List<RecipeList> commonRecipes = Arrays.asList(player.getCommonRecipeBook());
		player.getManufactureItems().clear();
		
		for (ManufactureItem i : _items)
		{
			final RecipeList list = RecipeData.getInstance().getRecipeList(i.getRecipeId());
			if (!dwarfRecipes.contains(list) && !commonRecipes.contains(list))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Player " + player.getName() + " of account " + player.getAccountName() + " tried to set recipe which he dont have.", Config.DEFAULT_PUNISH);
				return;
			}
			
			if (i.getCost() > MAX_ADENA)
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to set price more than " + MAX_ADENA + " adena in Private Manufacture.", Config.DEFAULT_PUNISH);
				return;
			}
			
			player.getManufactureItems().put(i.getRecipeId(), i);
		}
		
		player.setStoreName(!player.hasManufactureShop() ? "" : player.getStoreName());
		player.setPrivateStoreType(PrivateStoreType.MANUFACTURE);
		player.sitDown();
		player.broadcastUserInfo();
		Broadcast.toSelfAndKnownPlayers(player, new RecipeShopMsg(player));
	}
}
