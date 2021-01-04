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
package org.lineage.gameserver.model.zone.type;

import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.Npc;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.zone.ZoneId;
import org.lineage.gameserver.model.zone.ZoneType;
import org.lineage.gameserver.network.serverpackets.AbstractNpcInfo;
import org.lineage.gameserver.network.serverpackets.FakePlayerInfo;
import org.lineage.gameserver.network.serverpackets.ServerObjectInfo;

public class WaterZone extends ZoneType
{
	public WaterZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		creature.setInsideZone(ZoneId.WATER, true);
		
		// TODO: update to only send speed status when that packet is known
		if (creature.isPlayer())
		{
			final PlayerInstance player = creature.getActingPlayer();
			if (player.isTransformed() && !player.getTransformation().canSwim())
			{
				creature.stopTransformation(true);
			}
			else
			{
				player.broadcastUserInfo();
			}
		}
		else if (creature.isNpc())
		{
			World.getInstance().forEachVisibleObject(creature, PlayerInstance.class, player ->
			{
				if (creature.isFakePlayer())
				{
					player.sendPacket(new FakePlayerInfo((Npc) creature));
				}
				else if (creature.getRunSpeed() == 0)
				{
					player.sendPacket(new ServerObjectInfo((Npc) creature, player));
				}
				else
				{
					player.sendPacket(new AbstractNpcInfo.NpcInfo((Npc) creature, player));
				}
			});
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		creature.setInsideZone(ZoneId.WATER, false);
		
		// TODO: update to only send speed status when that packet is known
		if (creature.isPlayer())
		{
			// Mobius: Attempt to stop water task.
			if (!creature.isInsideZone(ZoneId.WATER))
			{
				((PlayerInstance) creature).stopWaterTask();
			}
			if (!creature.isTeleporting())
			{
				creature.getActingPlayer().broadcastUserInfo();
			}
		}
		else if (creature.isNpc())
		{
			World.getInstance().forEachVisibleObject(creature, PlayerInstance.class, player ->
			{
				if (creature.isFakePlayer())
				{
					player.sendPacket(new FakePlayerInfo((Npc) creature));
				}
				else if (creature.getRunSpeed() == 0)
				{
					player.sendPacket(new ServerObjectInfo((Npc) creature, player));
				}
				else
				{
					player.sendPacket(new AbstractNpcInfo.NpcInfo((Npc) creature, player));
				}
			});
		}
	}
	
	public int getWaterZ()
	{
		return getZone().getHighZ();
	}
}
