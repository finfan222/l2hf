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

import org.lineage.Config;
import org.lineage.commons.util.Rnd;
import org.lineage.gameserver.enums.ShotType;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.effects.EffectType;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.model.stats.Formulas;
import org.lineage.gameserver.model.stats.Stat;

/**
 * Death Link effect implementation.
 * @author Adry_85
 */
public class DeathLink extends AbstractEffect
{
	public DeathLink(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.DEATH_LINK;
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
		final Creature creature = info.getEffector();
		if (creature.isAlikeDead())
		{
			return;
		}
		
		final boolean sps = info.getSkill().useSpiritShot() && creature.isChargedShot(ShotType.SPIRITSHOTS);
		final boolean bss = info.getSkill().useSpiritShot() && creature.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		if (target.isPlayer() && target.getActingPlayer().isFakeDeath() && Config.FAKE_DEATH_DAMAGE_STAND)
		{
			target.stopFakeDeath(true);
		}
		
		final boolean mcrit = Formulas.calcMCrit(creature.getMCriticalHit(target, info.getSkill()));
		final byte shld = Formulas.calcShldUse(creature, target, info.getSkill());
		final int damage = (int) Formulas.calcMagicDam(creature, target, info.getSkill(), shld, sps, bss, mcrit);
		if (damage > 0)
		{
			// Manage attack or cast break of the target (calculating rate, sending message...)
			if (!target.isRaid() && Formulas.calcAtkBreak(target, damage))
			{
				target.breakAttack();
				target.breakCast();
			}
			
			// Shield Deflect Magic: Reflect all damage on caster.
			if (target.getStat().calcStat(Stat.VENGEANCE_SKILL_MAGIC_DAMAGE, 0, target, info.getSkill()) > Rnd.get(100))
			{
				creature.reduceCurrentHp(damage, target, info.getSkill());
				creature.notifyDamageReceived(damage, target, info.getSkill(), mcrit, false);
			}
			else
			{
				target.reduceCurrentHp(damage, creature, info.getSkill());
				target.notifyDamageReceived(damage, creature, info.getSkill(), mcrit, false);
				creature.sendDamageMessage(target, damage, mcrit, false, false);
			}
		}
		
		if (info.getSkill().isSuicideAttack())
		{
			creature.doDie(creature);
		}
	}
}