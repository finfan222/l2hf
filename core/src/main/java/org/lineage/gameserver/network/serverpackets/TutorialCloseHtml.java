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
package org.lineage.gameserver.network.serverpackets;

import org.lineage.commons.network.PacketWriter;
import org.lineage.gameserver.enums.HtmlActionScope;
import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.network.OutgoingPackets;

/**
 * TutorialCloseHtml server packet implementation.
 * @author HorridoJoho
 */
public class TutorialCloseHtml implements IClientOutgoingPacket
{
	public static final TutorialCloseHtml STATIC_PACKET = new TutorialCloseHtml();
	
	private TutorialCloseHtml()
	{
	}
	
	@Override
	public void runImpl(PlayerInstance player)
	{
		player.clearHtmlActions(HtmlActionScope.TUTORIAL_HTML);
	}
	
	@Override
	public boolean write(PacketWriter packet)
	{
		OutgoingPackets.TUTORIAL_CLOSE_HTML.writeId(packet);
		return true;
	}
}