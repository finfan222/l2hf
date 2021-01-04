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
package org.lineage.gameserver.model.events.impl.creature.player;

import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.events.EventType;
import org.lineage.gameserver.model.events.impl.IBaseEvent;

/**
 * @author UnAfraid
 */
public class OnPlayerTransform implements IBaseEvent
{
	private final PlayerInstance _player;
	private final int _transformId;
	
	public OnPlayerTransform(PlayerInstance player, int transformId)
	{
		_player = player;
		_transformId = transformId;
	}
	
	public PlayerInstance getPlayer()
	{
		return _player;
	}
	
	public int getTransformId()
	{
		return _transformId;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_PLAYER_TRANSFORM;
	}
}
