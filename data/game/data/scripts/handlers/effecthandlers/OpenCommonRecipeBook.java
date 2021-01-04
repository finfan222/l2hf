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

import org.lineage.gameserver.RecipeController;
import org.lineage.gameserver.enums.PrivateStoreType;
import org.lineage.gameserver.model.StatSet;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.conditions.Condition;
import org.lineage.gameserver.model.effects.AbstractEffect;
import org.lineage.gameserver.model.skills.BuffInfo;
import org.lineage.gameserver.network.SystemMessageId;

/**
 * Open Common Recipe Book effect implementation.
 * @author Adry_85
 */
public class OpenCommonRecipeBook extends AbstractEffect
{
	public OpenCommonRecipeBook(Condition attachCond, Condition applyCond, StatSet set, StatSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		if (!info.getEffector().isPlayer())
		{
			return;
		}
		
		final PlayerInstance player = info.getEffector().getActingPlayer();
		if (player.getPrivateStoreType() != PrivateStoreType.NONE)
		{
			player.sendPacket(SystemMessageId.ITEM_CREATION_IS_NOT_POSSIBLE_WHILE_ENGAGED_IN_A_TRADE);
			return;
		}
		
		RecipeController.getInstance().requestBookOpen(player, false);
	}
}
