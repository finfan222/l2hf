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
package org.lineage.gameserver.model.events.impl.item;

import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.events.EventType;
import org.lineage.gameserver.model.events.impl.IBaseEvent;
import org.lineage.gameserver.model.items.instance.ItemInstance;

/**
 * @author UnAfraid
 */
public class OnItemBypassEvent implements IBaseEvent
{
	private final ItemInstance _item;
	private final PlayerInstance _player;
	private final String _event;
	
	public OnItemBypassEvent(ItemInstance item, PlayerInstance player, String event)
	{
		_item = item;
		_player = player;
		_event = event;
	}
	
	public ItemInstance getItem()
	{
		return _item;
	}
	
	public PlayerInstance getActiveChar()
	{
		return _player;
	}
	
	public String getEvent()
	{
		return _event;
	}
	
	@Override
	public EventType getType()
	{
		return EventType.ON_ITEM_BYPASS_EVENT;
	}
}
