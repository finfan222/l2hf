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
package org.lineage.gameserver.handler;

import org.lineage.gameserver.enums.InstanceType;
import org.lineage.gameserver.model.WorldObject;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;

import java.util.logging.Logger;

public interface IActionHandler
{
	Logger LOGGER = Logger.getLogger(IActionHandler.class.getName());
	
	boolean action(PlayerInstance player, WorldObject target, boolean interact);
	
	InstanceType getInstanceType();
}