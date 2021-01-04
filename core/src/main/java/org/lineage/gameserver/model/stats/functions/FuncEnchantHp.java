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
package org.lineage.gameserver.model.stats.functions;

import org.lineage.gameserver.data.xml.impl.EnchantItemHPBonusData;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.items.instance.ItemInstance;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.model.stats.Stat;

/**
 * @author Yamaneko
 */
public class FuncEnchantHp extends AbstractFunction
{
	public FuncEnchantHp(Stat stat, int order, Object owner, double value, Condition applayCond)
	{
		super(stat, order, owner, value, applayCond);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, Skill skill, double initVal)
	{
		if ((getApplayCond() != null) && !getApplayCond().test(effector, effected, skill))
		{
			return initVal;
		}
		
		final ItemInstance item = (ItemInstance) getFuncOwner();
		if (item.getEnchantLevel() > 0)
		{
			return initVal + EnchantItemHPBonusData.getInstance().getHPBonus(item);
		}
		return initVal;
	}
}
