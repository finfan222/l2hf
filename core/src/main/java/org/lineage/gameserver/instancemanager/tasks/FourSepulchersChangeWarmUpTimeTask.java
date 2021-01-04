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
package org.lineage.gameserver.instancemanager.tasks;

import org.lineage.Config;
import org.lineage.commons.concurrent.ThreadPool;
import org.lineage.gameserver.instancemanager.FourSepulchersManager;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;

/**
 * Four Sepulchers change warm up time task.
 * @author xban1x
 */
public class FourSepulchersChangeWarmUpTimeTask implements Runnable
{
	@Override
	public void run()
	{
		final FourSepulchersManager manager = FourSepulchersManager.getInstance();
		manager.setEntryTime(true);
		manager.setWarmUpTime(false);
		manager.setAttackTime(false);
		manager.setCoolDownTime(false);
		
		final long interval = manager.isFirstTimeRun() ? manager.getWarmUpTimeEnd() - Calendar.getInstance().getTimeInMillis() : Config.FS_TIME_WARMUP * 60000;
		manager.setChangeAttackTimeTask(ThreadPool.schedule(new FourSepulchersChangeAttackTimeTask(), interval));
		final ScheduledFuture<?> changeWarmUpTimeTask = manager.getChangeWarmUpTimeTask();
		
		if (changeWarmUpTimeTask != null)
		{
			changeWarmUpTimeTask.cancel(true);
			manager.setChangeWarmUpTimeTask(null);
		}
	}
}
