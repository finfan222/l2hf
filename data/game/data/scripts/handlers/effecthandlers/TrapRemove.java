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

import org.lineage.gameserver.enums.TrapAction;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.instance.TrapInstance;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.events.EventDispatcher;
import org.lineage.gameserver.model.events.impl.creature.trap.OnTrapAction;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.network.SystemMessageId;

/**
 * Trap Remove effect implementation.
 * @author UnAfraid
 */
public class TrapRemove extends AbstractEffect
{
	private final int _power;
	
	public TrapRemove(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
		
		if (params.isEmpty())
		{
			throw new IllegalArgumentException(getClass().getSimpleName() + ": effect without power!");
		}
		
		_power = params.getInt("power");
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		final Creature target = info.getEffected();
		if (!target.isTrap())
		{
			return;
		}
		
		if (target.isAlikeDead())
		{
			return;
		}
		
		final TrapInstance trap = (TrapInstance) target;
		if (!trap.canBeSeen(info.getEffector()))
		{
			if (info.getEffector().isPlayer())
			{
				info.getEffector().sendPacket(SystemMessageId.INVALID_TARGET);
			}
			return;
		}
		
		if (trap.getLevel() > _power)
		{
			return;
		}
		
		// Notify to scripts
		EventDispatcher.getInstance().notifyEventAsync(new OnTrapAction(trap, info.getEffector(), TrapAction.TRAP_DISARMED), trap);
		trap.unSummon();
		if (info.getEffector().isPlayer())
		{
			info.getEffector().sendPacket(SystemMessageId.THE_TRAP_DEVICE_HAS_BEEN_STOPPED);
		}
	}
}
