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
package handlers.bypasshandlers;

import org.lineage.gameserver.handler.IBypassHandler;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.Npc;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.skills.CommonSkill;

public class SupportBlessing implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"GiveBlessing"
	};
	
	@Override
	public boolean useBypass(String command, PlayerInstance player, Creature target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		final Npc npc = (Npc) target;
		
		// If the player is too high level, display a message and return
		if ((player.getLevel() > 39) || (player.getClassId().level() >= 2))
		{
			npc.showChatWindow(player, "data/html/default/SupportBlessingHighLevel.htm");
			return true;
		}
		npc.setTarget(player);
		npc.doCast(CommonSkill.BLESSING_OF_PROTECTION.getSkill());
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}