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
package handlers.actionhandlers;

import org.lineage.gameserver.ai.CtrlIntention;
import org.lineage.gameserver.enums.InstanceType;
import org.lineage.gameserver.geoengine.GeoEngine;
import org.lineage.gameserver.handler.IActionHandler;
import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.Summon;
import org.lineage.gameserver.model.actor.instance.PetInstance;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.events.EventDispatcher;
import org.lineage.gameserver.model.events.impl.creature.player.OnPlayerSummonTalk;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.PetStatusShow;

public class PetInstanceAction implements IActionHandler
{
	@Override
	public boolean action(PlayerInstance player, WorldObject target, boolean interact)
	{
		// Aggression target lock effect
		if (player.isLockedTarget() && (player.getLockedTarget() != target))
		{
			player.sendPacket(SystemMessageId.FAILED_TO_CHANGE_ATTACK_TARGET);
			return false;
		}
		
		final boolean isOwner = player.getObjectId() == ((PetInstance) target).getOwner().getObjectId();
		if (isOwner && (player != ((PetInstance) target).getOwner()))
		{
			((PetInstance) target).updateRefOwner(player);
		}
		if (player.getTarget() != target)
		{
			// Set the target of the PlayerInstance player
			player.setTarget(target);
		}
		else if (interact)
		{
			// Check if the pet is attackable (without a forced attack) and isn't dead
			if (target.isAutoAttackable(player) && !isOwner)
			{
				if (GeoEngine.getInstance().canSeeTarget(player, target))
				{
					// Set the PlayerInstance Intention to AI_INTENTION_ATTACK
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
					player.onActionRequest();
				}
			}
			else if (!((Creature) target).isInsideRadius2D(player, 150))
			{
				if (GeoEngine.getInstance().canSeeTarget(player, target))
				{
					player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
					player.onActionRequest();
				}
			}
			else
			{
				if (isOwner)
				{
					player.sendPacket(new PetStatusShow((PetInstance) target));
					
					// Notify to scripts
					EventDispatcher.getInstance().notifyEventAsync(new OnPlayerSummonTalk((Summon) target), (Summon) target);
				}
				player.updateNotMoveUntil();
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.PetInstance;
	}
}