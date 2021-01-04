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

import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.actor.Attackable;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.model.stats.Formulas;

/**
 * Transfer Hate effect implementation.
 * @author Adry_85
 */
public class TransferHate extends AbstractEffect
{
	private final int _chance;
	
	public TransferHate(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
		
		_chance = params.getInt("chance", 100);
	}
	
	@Override
	public boolean calcSuccess(BuffInfo info)
	{
		return Formulas.calcProbability(_chance, info.getEffector(), info.getEffected(), info.getSkill());
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		World.getInstance().forEachVisibleObjectInRange(info.getEffector(), Attackable.class, info.getSkill().getAffectRange(), hater ->
		{
			if ((hater == null) || hater.isDead())
			{
				return;
			}
			
			final int hate = hater.getHating(info.getEffector());
			if (hate <= 0)
			{
				return;
			}
			
			hater.reduceHate(info.getEffector(), -hate);
			hater.addDamageHate(info.getEffected(), 0, hate);
		});
	}
}
