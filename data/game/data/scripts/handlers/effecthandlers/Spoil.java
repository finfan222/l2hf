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

import org.lineage.gameserver.ai.CtrlEvent;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.actor.instance.MonsterInstance;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.model.stats.Formulas;
import org.lineage.gameserver.network.SystemMessageId;

/**
 * Spoil effect implementation.
 * @author _drunk_, Ahmed, Zoey76
 */
public class Spoil extends AbstractEffect
{
	public Spoil(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public boolean calcSuccess(BuffInfo info)
	{
		return Formulas.calcMagicSuccess(info.getEffector(), info.getEffected(), info.getSkill());
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		if (!info.getEffected().isMonster() || info.getEffected().isDead())
		{
			info.getEffector().sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		final MonsterInstance target = (MonsterInstance) info.getEffected();
		if (target.isSpoiled())
		{
			info.getEffector().sendPacket(SystemMessageId.IT_HAS_ALREADY_BEEN_SPOILED);
			return;
		}
		
		target.setSpoilerObjectId(info.getEffector().getObjectId());
		info.getEffector().sendPacket(SystemMessageId.THE_SPOIL_CONDITION_HAS_BEEN_ACTIVATED);
		target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, info.getEffector());
	}
}
