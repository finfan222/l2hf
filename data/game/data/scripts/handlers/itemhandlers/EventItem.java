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

import org.lineage.gameserver.handler.IItemHandler;
import org.lineage.gameserver.instancemanager.HandysBlockCheckerManager;
import org.lineage.gameserver.model.ArenaParticipantsHolder;
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.actor.Playable;
import org.lineage.gameserver.model.actor.instance.BlockInstance;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.SystemMessage;

public class EventItem implements IItemHandler
{
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		boolean used = false;
		
		final PlayerInstance player = playable.getActingPlayer();
		final int itemId = item.getId();
		switch (itemId)
		{
			case 13787: // Handy's Block Checker Bond
			{
				used = useBlockCheckerItem(player, item);
				break;
			}
			case 13788: // Handy's Block Checker Land Mine
			{
				used = useBlockCheckerItem(player, item);
				break;
			}
			default:
			{
				LOGGER.warning("EventItemHandler: Item with id: " + itemId + " is not handled");
			}
		}
		return used;
	}
	
	private final boolean useBlockCheckerItem(PlayerInstance castor, ItemInstance item)
	{
		final int blockCheckerArena = castor.getBlockCheckerArena();
		if (blockCheckerArena == -1)
		{
			final SystemMessage msg = new SystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
			msg.addItemName(item);
			castor.sendPacket(msg);
			return false;
		}
		
		final Skill sk = item.getEtcItem().getSkills()[0].getSkill();
		if (sk == null)
		{
			return false;
		}
		
		if (!castor.destroyItem("Consume", item, 1, castor, true))
		{
			return false;
		}
		
		final BlockInstance block = (BlockInstance) castor.getTarget();
		final ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(blockCheckerArena);
		if (holder != null)
		{
			final int team = holder.getPlayerTeam(castor);
			World.getInstance().forEachVisibleObjectInRange(block, PlayerInstance.class, sk.getEffectRange(), pc ->
			{
				final int enemyTeam = holder.getPlayerTeam(pc);
				if ((enemyTeam != -1) && (enemyTeam != team))
				{
					sk.applyEffects(castor, pc);
				}
			});
			return true;
		}
		LOGGER.warning("Char: " + castor.getName() + "[" + castor.getObjectId() + "] has unknown block checker arena");
		return false;
	}
}
