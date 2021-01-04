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
package handlers.itemhandlers;

import java.util.List;

import org.lineage.Config;
import org.lineage.commons.util.Rnd;
import org.lineage.gameserver.datatables.ItemTable;
import org.lineage.gameserver.handler.IItemHandler;
import org.lineage.gameserver.model.ExtractableProduct;
import org.lineage.gameserver.model.actor.Playable;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.items.EtcItem;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.network.SystemMessageId;

/**
 * Extractable Items handler.
 * @author HorridoJoho
 */
public class ExtractableItems implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final PlayerInstance player = playable.getActingPlayer();
		final EtcItem etcitem = (EtcItem) item.getItem();
		final List<ExtractableProduct> exitem = etcitem.getExtractableItems();
		if (exitem == null)
		{
			LOGGER.info("No extractable data defined for " + etcitem);
			return false;
		}
		
		// destroy item
		if (!player.destroyItem("Extract", item.getObjectId(), 1, player, true))
		{
			return false;
		}
		
		boolean created = false;
		for (ExtractableProduct expi : exitem)
		{
			if (Rnd.get(100000) <= expi.getChance())
			{
				final int min = (int) (expi.getMin() * Config.RATE_EXTRACTABLE);
				final int max = (int) (expi.getMax() * Config.RATE_EXTRACTABLE);
				int createItemAmount = (max == min) ? min : (Rnd.get((max - min) + 1) + min);
				if (createItemAmount == 0)
				{
					continue;
				}
				
				if (ItemTable.getInstance().getTemplate(expi.getId()).isStackable() || (createItemAmount == 1))
				{
					player.addItem("Extract", expi.getId(), createItemAmount, player, true);
				}
				else
				{
					while (createItemAmount > 0)
					{
						player.addItem("Extract", expi.getId(), 1, player, true);
						createItemAmount--;
					}
				}
				created = true;
			}
		}
		
		if (!created)
		{
			player.sendPacket(SystemMessageId.THERE_WAS_NOTHING_FOUND_INSIDE);
		}
		return true;
	}
}
