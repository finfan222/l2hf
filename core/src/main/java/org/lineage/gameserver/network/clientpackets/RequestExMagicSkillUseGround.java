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
package org.lineage.gameserver.network.clientpackets;

import org.lineage.commons.network.PacketReader;
import org.lineage.gameserver.data.xml.impl.SkillData;
import org.lineage.gameserver.model.Location;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.network.GameClient;
import org.lineage.gameserver.network.serverpackets.ActionFailed;
import org.lineage.gameserver.network.serverpackets.ValidateLocation;
import org.lineage.gameserver.util.Broadcast;
import org.lineage.gameserver.util.Util;

/**
 * Fromat:(ch) dddddc
 * @author -Wooden-
 */
public class RequestExMagicSkillUseGround implements IClientIncomingPacket
{
	private int _x;
	private int _y;
	private int _z;
	private int _skillId;
	private boolean _ctrlPressed;
	private boolean _shiftPressed;
	
	@Override
	public boolean read(GameClient client, PacketReader packet)
	{
		_x = packet.readD();
		_y = packet.readD();
		_z = packet.readD();
		_skillId = packet.readD();
		_ctrlPressed = packet.readD() != 0;
		_shiftPressed = packet.readC() != 0;
		return true;
	}
	
	@Override
	public void run(GameClient client)
	{
		// Get the current PlayerInstance of the player
		final PlayerInstance player = client.getPlayer();
		if (player == null)
		{
			return;
		}
		
		// Get the level of the used skill
		final int level = player.getSkillLevel(_skillId);
		if (level <= 0)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Get the Skill template corresponding to the skillID received from the client
		final Skill skill = SkillData.getInstance().getSkill(_skillId, level);
		
		// Check the validity of the skill
		if (skill != null)
		{
			player.setCurrentSkillWorldPosition(new Location(_x, _y, _z));
			
			// normally magicskilluse packet turns char client side but for these skills, it doesn't (even with correct target)
			player.setHeading(Util.calculateHeadingFrom(player.getX(), player.getY(), _x, _y));
			Broadcast.toKnownPlayers(player, new ValidateLocation(player));
			player.useMagic(skill, _ctrlPressed, _shiftPressed);
		}
		else
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			LOGGER.warning("No skill found with id " + _skillId + " and level " + level + " !!");
		}
	}
}