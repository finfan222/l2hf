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
package ai.areas.FantasyIsle;

import org.lineage.Config;
import org.lineage.gameserver.instancemanager.HandysBlockCheckerManager;
import org.lineage.gameserver.model.ArenaParticipantsHolder;
import org.lineage.gameserver.model.actor.Npc;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.ExCubeGameChangeTimeToStart;
import org.lineage.gameserver.network.serverpackets.ExCubeGameRequestReady;
import org.lineage.gameserver.network.serverpackets.ExCubeGameTeamList;

import ai.AbstractNpcAI;

/**
 * Handys Block Checker Event AI.
 * @authors BiggBoss, Gigiikun
 */
public class HandysBlockCheckerEvent extends AbstractNpcAI
{
	// Arena Managers
	private static final int A_MANAGER_1 = 32521;
	private static final int A_MANAGER_2 = 32522;
	private static final int A_MANAGER_3 = 32523;
	private static final int A_MANAGER_4 = 32524;
	
	public HandysBlockCheckerEvent()
	{
		addFirstTalkId(A_MANAGER_1, A_MANAGER_2, A_MANAGER_3, A_MANAGER_4);
		HandysBlockCheckerManager.getInstance().startUpParticipantsQueue();
	}
	
	@Override
	public String onFirstTalk(Npc npc, PlayerInstance player)
	{
		if ((npc == null) || (player == null))
		{
			return null;
		}
		
		final int arena = npc.getId() - A_MANAGER_1;
		if (eventIsFull(arena))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_REGISTER_BECAUSE_CAPACITY_HAS_BEEN_EXCEEDED);
			return null;
		}
		
		if (HandysBlockCheckerManager.getInstance().arenaIsBeingUsed(arena))
		{
			player.sendPacket(SystemMessageId.THE_MATCH_IS_BEING_PREPARED_PLEASE_TRY_AGAIN_LATER);
			return null;
		}
		
		if (HandysBlockCheckerManager.getInstance().addPlayerToArena(player, arena))
		{
			final ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(arena);
			player.sendPacket(new ExCubeGameTeamList(holder.getRedPlayers(), holder.getBluePlayers(), arena));
			if ((holder.getBlueTeamSize() >= Config.MIN_BLOCK_CHECKER_TEAM_MEMBERS) && (holder.getRedTeamSize() >= Config.MIN_BLOCK_CHECKER_TEAM_MEMBERS))
			{
				holder.updateEvent();
				holder.broadCastPacketToTeam(new ExCubeGameRequestReady());
				holder.broadCastPacketToTeam(new ExCubeGameChangeTimeToStart(10));
			}
		}
		return null;
	}
	
	private boolean eventIsFull(int arena)
	{
		return HandysBlockCheckerManager.getInstance().getHolder(arena).getAllPlayers().size() == 12;
	}
	
	public static void main(String[] args)
	{
		if (Config.ENABLE_BLOCK_CHECKER_EVENT)
		{
			new HandysBlockCheckerEvent();
			LOGGER.info("Handy's Block Checker Event is enabled");
		}
		else
		{
			LOGGER.info("Handy's Block Checker Event is disabled");
		}
	}
}