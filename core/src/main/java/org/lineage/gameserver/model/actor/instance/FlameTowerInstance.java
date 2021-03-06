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
package org.lineage.gameserver.model.actor.instance;

import org.lineage.gameserver.enums.InstanceType;
import org.lineage.gameserver.instancemanager.ZoneManager;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.Tower;
import org.lineage.gameserver.model.actor.templates.NpcTemplate;
import org.lineage.gameserver.model.zone.ZoneType;

import java.util.List;

/**
 * Class for Flame Control Tower instance.
 * @author JIV
 */
public class FlameTowerInstance extends Tower
{
	private int _upgradeLevel = 0;
	private List<Integer> _zoneList;
	
	/**
	 * Creates a flame tower.
	 * @param template the flame tower NPC template
	 */
	public FlameTowerInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.FlameTowerInstance);
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		enableZones(false);
		return super.doDie(killer);
	}
	
	@Override
	public boolean deleteMe()
	{
		enableZones(false);
		return super.deleteMe();
	}
	
	public void enableZones(boolean value)
	{
		if ((_zoneList != null) && (_upgradeLevel != 0))
		{
			final int maxIndex = _upgradeLevel * 2;
			for (int i = 0; i < maxIndex; i++)
			{
				final ZoneType zone = ZoneManager.getInstance().getZoneById(_zoneList.get(i));
				if (zone != null)
				{
					zone.setEnabled(value);
				}
			}
		}
	}
	
	public void setUpgradeLevel(int level)
	{
		_upgradeLevel = level;
	}
	
	public void setZoneList(List<Integer> list)
	{
		_zoneList = list;
		enableZones(true);
	}
}