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

import org.lineage.Config;
import org.lineage.gameserver.data.xml.impl.SkillData;
import org.lineage.gameserver.data.xml.impl.SkillTreeData;
import org.lineage.gameserver.enums.InstanceType;
import org.lineage.gameserver.instancemanager.FishingChampionshipManager;
import org.lineage.gameserver.model.SkillLearn;
import org.lineage.gameserver.model.actor.Npc;
import org.lineage.gameserver.model.actor.templates.NpcTemplate;
import org.lineage.gameserver.model.base.AcquireSkillType;
import org.lineage.gameserver.model.skills.Skill;
import org.lineage.gameserver.network.SystemMessageId;
import org.lineage.gameserver.network.serverpackets.AcquireSkillList;
import org.lineage.gameserver.network.serverpackets.NpcHtmlMessage;
import org.lineage.gameserver.network.serverpackets.SystemMessage;

import java.util.List;

public class FishermanInstance extends MerchantInstance
{
	/**
	 * Creates a fisherman.
	 * @param template the fisherman NPC template
	 */
	public FishermanInstance(NpcTemplate template)
	{
		super(template);
		setInstanceType(InstanceType.FishermanInstance);
	}
	
	@Override
	public String getHtmlPath(int npcId, int value)
	{
		String pom = "";
		if (value == 0)
		{
			pom = Integer.toString(npcId);
		}
		else
		{
			pom = npcId + "-" + value;
		}
		return "data/html/fisherman/" + pom + ".htm";
	}
	
	@Override
	public void onBypassFeedback(PlayerInstance player, String command)
	{
		if (command.equalsIgnoreCase("FishSkillList"))
		{
			showFishSkillList(player);
		}
		else if (command.startsWith("FishingChampionship"))
		{
			if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
			{
				FishingChampionshipManager.getInstance().showChampScreen(player, this);
			}
			else
			{
				sendHtml(player, this, "no_fish_event001.htm");
			}
		}
		else if (command.startsWith("FishingReward"))
		{
			if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
			{
				if (FishingChampionshipManager.getInstance().isWinner(player.getName()))
				{
					FishingChampionshipManager.getInstance().getReward(player);
				}
				else
				{
					sendHtml(player, this, "no_fish_event_reward001.htm");
				}
			}
			else
			{
				sendHtml(player, this, "no_fish_event001.htm");
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	public static void showFishSkillList(PlayerInstance player)
	{
		final List<SkillLearn> skills = SkillTreeData.getInstance().getAvailableFishingSkills(player);
		final AcquireSkillList asl = new AcquireSkillList(AcquireSkillType.FISHING);
		int count = 0;
		for (SkillLearn s : skills)
		{
			final Skill sk = SkillData.getInstance().getSkill(s.getSkillId(), s.getSkillLevel());
			if (sk == null)
			{
				continue;
			}
			count++;
			asl.addSkill(s.getSkillId(), s.getSkillLevel(), s.getSkillLevel(), s.getLevelUpSp(), 1);
		}
		
		if (count == 0)
		{
			final int minlLevel = SkillTreeData.getInstance().getMinLevelForNewSkill(player, SkillTreeData.getInstance().getFishingSkillTree());
			if (minlLevel > 0)
			{
				final SystemMessage sm = new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ANY_FURTHER_SKILLS_TO_LEARN_COME_BACK_WHEN_YOU_HAVE_REACHED_LEVEL_S1);
				sm.addInt(minlLevel);
				player.sendPacket(sm);
			}
			else
			{
				player.sendPacket(SystemMessageId.THERE_ARE_NO_OTHER_SKILLS_TO_LEARN);
			}
		}
		else
		{
			player.sendPacket(asl);
		}
	}
	
	private void sendHtml(PlayerInstance player, Npc npc, String htmlName)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile(player, "data/html/fisherman/championship/" + htmlName);
		player.sendPacket(html);
	}
}
