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
package org.lineage.gameserver.taskmanager.tasks;

import org.lineage.Config;
import org.lineage.gameserver.data.sql.impl.ClanTable;
import org.lineage.gameserver.model.clan.Clan;
import org.lineage.gameserver.model.clan.ClanMember;
import org.lineage.gameserver.taskmanager.Task;
import org.lineage.gameserver.taskmanager.TaskManager;
import org.lineage.gameserver.taskmanager.TaskManager.ExecutedTask;
import org.lineage.gameserver.taskmanager.TaskTypes;

import java.util.Calendar;

/**
 * @author UnAfraid
 */
public class TaskClanLeaderApply extends Task
{
	private static final String NAME = "clanleaderapply";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != Config.ALT_CLAN_LEADER_DATE_CHANGE)
		{
			return;
		}
		
		for (Clan clan : ClanTable.getInstance().getClans())
		{
			if (clan.getNewLeaderId() != 0)
			{
				final ClanMember member = clan.getClanMember(clan.getNewLeaderId());
				if (member == null)
				{
					continue;
				}
				
				clan.setNewLeader(member);
			}
		}
		LOGGER.info(getClass().getSimpleName() + ": launched.");
	}
	
	@Override
	public void initializate()
	{
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", Config.ALT_CLAN_LEADER_HOUR_CHANGE, "");
	}
}
