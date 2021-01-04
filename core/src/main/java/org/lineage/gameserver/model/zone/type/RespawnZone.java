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
package org.lineage.gameserver.model.zone.type;

import org.lineage.gameserver.enums.Race;
import org.lineage.gameserver.model.actor.Creature;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.zone.ZoneType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Respawn zone implementation.
 * @author Nyaran
 */
public class RespawnZone extends ZoneType
{
	private final Map<Race, String> _raceRespawnPoint = new EnumMap<>(Race.class);
	
	public RespawnZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
	}
	
	@Override
	protected void onExit(Creature creature)
	{
	}
	
	public void addRaceRespawnPoint(String race, String point)
	{
		_raceRespawnPoint.put(Race.valueOf(race), point);
	}
	
	public Map<Race, String> getAllRespawnPoints()
	{
		return _raceRespawnPoint;
	}
	
	public String getRespawnPoint(PlayerInstance player)
	{
		return _raceRespawnPoint.get(player.getRace());
	}
}