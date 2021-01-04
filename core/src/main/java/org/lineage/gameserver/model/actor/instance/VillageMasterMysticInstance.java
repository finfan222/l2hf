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

import org.lineage.gameserver.data.xml.impl.CategoryData;
import org.lineage.gameserver.enums.CategoryType;
import org.lineage.gameserver.enums.Race;
import org.lineage.gameserver.model.actor.templates.NpcTemplate;
import org.lineage.gameserver.model.base.ClassId;

public class VillageMasterMysticInstance extends VillageMasterInstance
{
	/**
	 * Creates a village master.
	 * @param template the village master NPC template
	 */
	public VillageMasterMysticInstance(NpcTemplate template)
	{
		super(template);
	}
	
	@Override
	protected final boolean checkVillageMasterRace(ClassId pClass)
	{
		if (pClass == null)
		{
			return false;
		}
		return (pClass.getRace() == Race.HUMAN) || (pClass.getRace() == Race.ELF);
	}
	
	@Override
	protected final boolean checkVillageMasterTeachType(ClassId pClass)
	{
		if (pClass == null)
		{
			return false;
		}
		return CategoryData.getInstance().isInCategory(CategoryType.MAGE_GROUP, pClass.getId());
	}
}