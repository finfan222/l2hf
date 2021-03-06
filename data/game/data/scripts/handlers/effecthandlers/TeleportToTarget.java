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
package handlers.effecthandlers;

import org.lineage.gameserver.ai.CtrlIntention;
import org.lineage.gameserver.geoengine.GeoEngine;
import org.lineage.gameserver.model.Location;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.effects.EffectType;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.network.serverpackets.FlyToLocation;
import org.lineage.gameserver.network.serverpackets.FlyToLocation.FlyType;
import org.lineage.gameserver.network.serverpackets.ValidateLocation;
import org.lineage.gameserver.util.Util;

/**
 * Teleport To Target effect implementation.
 * @author Didldak, Adry_85
 */
public class TeleportToTarget extends AbstractEffect
{
	public TeleportToTarget(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.TELEPORT_TO_TARGET;
	}
	
	@Override
	public boolean canStart(BuffInfo info)
	{
		return (info.getEffected() != null) && GeoEngine.getInstance().canSeeTarget(info.getEffected(), info.getEffector());
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		final Creature creature = info.getEffector();
		final Creature target = info.getEffected();
		if (target == null)
		{
			return;
		}
		
		final int px = target.getX();
		final int py = target.getY();
		double ph = Util.convertHeadingToDegree(target.getHeading());
		ph += 180;
		if (ph > 360)
		{
			ph -= 360;
		}
		
		ph = (Math.PI * ph) / 180;
		final int x = (int) (px + (25 * Math.cos(ph)));
		final int y = (int) (py + (25 * Math.sin(ph)));
		final int z = target.getZ();
		final Location loc = GeoEngine.getInstance().canMoveToTargetLoc(creature.getX(), creature.getY(), creature.getZ(), x, y, z, creature.getInstanceId());
		creature.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		creature.broadcastPacket(new FlyToLocation(creature, loc.getX(), loc.getY(), loc.getZ(), FlyType.DUMMY));
		creature.abortAttack();
		creature.abortCast();
		creature.setXYZ(loc);
		creature.broadcastPacket(new ValidateLocation(creature));
	}
}
