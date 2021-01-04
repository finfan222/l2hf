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
import org.lineage.gameserver.model.World;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.serverpackets.ExNevitAdventTimeChange;
import org.lineage.gameserver.taskmanager.Task;
import org.lineage.gameserver.taskmanager.TaskManager;
import org.lineage.gameserver.taskmanager.TaskManager.ExecutedTask;
import org.lineage.gameserver.taskmanager.TaskTypes;

/**
 * @author Janiko
 */
public class TaskNevit extends Task
{
	private static final String NAME = "nevit_system";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		if (!Config.NEVIT_ENABLED)
		{
			return;
		}
		
		for (PlayerInstance player : World.getInstance().getPlayers())
		{
			if ((player == null) || !player.isOnline())
			{
				continue;
			}
			
			player.getVariables().set("hunting_time", 0);
			player.sendPacket(new ExNevitAdventTimeChange(0, true));
		}
		LOGGER.info("Nevit system reseted.");
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "06:30:00", "");
	}
}
