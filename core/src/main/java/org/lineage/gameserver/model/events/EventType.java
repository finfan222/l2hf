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
package org.lineage.gameserver.model.events;

import org.lineage.commons.util.CommonUtil;
import org.lineage.gameserver.model.events.impl.IBaseEvent;
import org.lineage.gameserver.model.events.impl.clan.OnClanWarFinish;
import org.lineage.gameserver.model.events.impl.clan.OnClanWarStart;
import org.lineage.gameserver.model.events.impl.creature.*;
import org.lineage.gameserver.model.events.impl.creature.npc.*;
import org.lineage.gameserver.model.events.impl.creature.npc.attackable.*;
import org.lineage.gameserver.model.events.impl.creature.playable.OnPlayableExpChanged;
import org.lineage.gameserver.model.events.impl.creature.player.*;
import org.lineage.gameserver.model.events.impl.creature.player.clan.*;
import org.lineage.gameserver.model.events.impl.creature.player.clanwh.OnPlayerClanWHItemAdd;
import org.lineage.gameserver.model.events.impl.creature.player.clanwh.OnPlayerClanWHItemDestroy;
import org.lineage.gameserver.model.events.impl.creature.player.clanwh.OnPlayerClanWHItemTransfer;
import org.lineage.gameserver.model.events.impl.creature.player.inventory.*;
import org.lineage.gameserver.model.events.impl.creature.trap.OnTrapAction;
import org.lineage.gameserver.model.events.impl.events.OnTvTEventFinish;
import org.lineage.gameserver.model.events.impl.events.OnTvTEventKill;
import org.lineage.gameserver.model.events.impl.events.OnTvTEventRegistrationStart;
import org.lineage.gameserver.model.events.impl.events.OnTvTEventStart;
import org.lineage.gameserver.model.events.impl.item.OnItemBypassEvent;
import org.lineage.gameserver.model.events.impl.item.OnItemCreate;
import org.lineage.gameserver.model.events.impl.item.OnItemTalk;
import org.lineage.gameserver.model.events.impl.olympiad.OnOlympiadMatchResult;
import org.lineage.gameserver.model.events.impl.sieges.castle.OnCastleSiegeFinish;
import org.lineage.gameserver.model.events.impl.sieges.castle.OnCastleSiegeOwnerChange;
import org.lineage.gameserver.model.events.impl.sieges.castle.OnCastleSiegeStart;
import org.lineage.gameserver.model.events.impl.sieges.fort.OnFortSiegeFinish;
import org.lineage.gameserver.model.events.impl.sieges.fort.OnFortSiegeStart;
import org.lineage.gameserver.model.events.returns.ChatFilterReturn;
import org.lineage.gameserver.model.events.returns.TerminateReturn;

/**
 * @author UnAfraid
 */
public enum EventType
{
	// Attackable events
	ON_ATTACKABLE_AGGRO_RANGE_ENTER(OnAttackableAggroRangeEnter.class, void.class),
	ON_ATTACKABLE_ATTACK(OnAttackableAttack.class, void.class),
	ON_ATTACKABLE_FACTION_CALL(OnAttackableFactionCall.class, void.class),
	ON_ATTACKABLE_KILL(OnAttackableKill.class, void.class),
	
	// Castle events
	ON_CASTLE_SIEGE_FINISH(OnCastleSiegeFinish.class, void.class),
	ON_CASTLE_SIEGE_OWNER_CHANGE(OnCastleSiegeOwnerChange.class, void.class),
	ON_CASTLE_SIEGE_START(OnCastleSiegeStart.class, void.class),
	
	// Clan events
	ON_CLAN_WAR_FINISH(OnClanWarFinish.class, void.class),
	ON_CLAN_WAR_START(OnClanWarStart.class, void.class),
	
	// Creature events
	ON_CREATURE_ATTACK(OnCreatureAttack.class, void.class, TerminateReturn.class),
	ON_CREATURE_ATTACK_AVOID(OnCreatureAttackAvoid.class, void.class, void.class),
	ON_CREATURE_ATTACKED(OnCreatureAttacked.class, void.class, TerminateReturn.class),
	ON_CREATURE_DAMAGE_RECEIVED(OnCreatureDamageReceived.class, void.class),
	ON_CREATURE_DAMAGE_DEALT(OnCreatureDamageDealt.class, void.class),
	ON_CREATURE_KILL(OnCreatureKill.class, void.class, TerminateReturn.class),
	ON_CREATURE_SKILL_USE(OnCreatureSkillUse.class, void.class, TerminateReturn.class),
	ON_CREATURE_TELEPORTED(OnCreatureTeleported.class, void.class),
	ON_CREATURE_ZONE_ENTER(OnCreatureZoneEnter.class, void.class),
	ON_CREATURE_ZONE_EXIT(OnCreatureZoneExit.class, void.class),
	
	// Fortress events
	ON_FORT_SIEGE_FINISH(OnFortSiegeFinish.class, void.class),
	ON_FORT_SIEGE_START(OnFortSiegeStart.class, void.class),
	
	// Item events
	ON_ITEM_BYPASS_EVENT(OnItemBypassEvent.class, void.class),
	ON_ITEM_CREATE(OnItemCreate.class, void.class),
	ON_ITEM_TALK(OnItemTalk.class, void.class),
	
	// NPC events
	ON_NPC_CAN_BE_SEEN(OnNpcCanBeSeen.class, void.class, TerminateReturn.class),
	ON_NPC_CREATURE_SEE(OnNpcCreatureSee.class, void.class),
	ON_NPC_EVENT_RECEIVED(OnNpcEventReceived.class, void.class),
	ON_NPC_FIRST_TALK(OnNpcFirstTalk.class, void.class),
	ON_NPC_HATE(OnAttackableHate.class, void.class, TerminateReturn.class),
	ON_NPC_MOVE_FINISHED(OnNpcMoveFinished.class, void.class),
	ON_NPC_MOVE_NODE_ARRIVED(OnNpcMoveNodeArrived.class, void.class),
	ON_NPC_MOVE_ROUTE_FINISHED(OnNpcMoveRouteFinished.class, void.class),
	ON_NPC_QUEST_START(null, void.class),
	ON_NPC_SKILL_FINISHED(OnNpcSkillFinished.class, void.class),
	ON_NPC_SKILL_SEE(OnNpcSkillSee.class, void.class),
	ON_NPC_SPAWN(OnNpcSpawn.class, void.class),
	ON_NPC_TALK(null, void.class),
	ON_NPC_TELEPORT(OnNpcTeleport.class, void.class),
	ON_NPC_MANOR_BYPASS(OnNpcManorBypass.class, void.class),
	
	// Olympiad events
	ON_OLYMPIAD_MATCH_RESULT(OnOlympiadMatchResult.class, void.class),
	
	// Playable events
	ON_PLAYABLE_EXP_CHANGED(OnPlayableExpChanged.class, void.class, TerminateReturn.class),
	
	// Player events
	ON_PLAYER_AUGMENT(OnPlayerAugment.class, void.class),
	ON_PLAYER_BYPASS(OnPlayerBypass.class, void.class),
	ON_PLAYER_CHAT(OnPlayerChat.class, void.class, ChatFilterReturn.class),
	// Clan events
	ON_PLAYER_CLAN_CREATE(OnPlayerClanCreate.class, void.class),
	ON_PLAYER_CLAN_DESTROY(OnPlayerClanDestroy.class, void.class),
	ON_PLAYER_CLAN_JOIN(OnPlayerClanJoin.class, void.class),
	ON_PLAYER_CLAN_LEADER_CHANGE(OnPlayerClanLeaderChange.class, void.class),
	ON_PLAYER_CLAN_LEFT(OnPlayerClanLeft.class, void.class),
	ON_PLAYER_CLAN_LEVELUP(OnPlayerClanLvlUp.class, void.class),
	// Clan warehouse events
	ON_PLAYER_CLAN_WH_ITEM_ADD(OnPlayerClanWHItemAdd.class, void.class),
	ON_PLAYER_CLAN_WH_ITEM_DESTROY(OnPlayerClanWHItemDestroy.class, void.class),
	ON_PLAYER_CLAN_WH_ITEM_TRANSFER(OnPlayerClanWHItemTransfer.class, void.class),
	ON_PLAYER_CREATE(OnPlayerCreate.class, void.class),
	ON_PLAYER_DELETE(OnPlayerDelete.class, void.class),
	ON_PLAYER_DLG_ANSWER(OnPlayerDlgAnswer.class, void.class, TerminateReturn.class),
	ON_PLAYER_EQUIP_ITEM(OnPlayerEquipItem.class, void.class),
	ON_PLAYER_FAME_CHANGED(OnPlayerFameChanged.class, void.class),
	// Henna events
	ON_PLAYER_HENNA_ADD(OnPlayerHennaAdd.class, void.class),
	ON_PLAYER_HENNA_REMOVE(OnPlayerHennaRemove.class, void.class),
	// Inventory events
	ON_PLAYER_ITEM_ADD(OnPlayerItemAdd.class, void.class),
	ON_PLAYER_ITEM_DESTROY(OnPlayerItemDestroy.class, void.class),
	ON_PLAYER_ITEM_DROP(OnPlayerItemDrop.class, void.class),
	ON_PLAYER_ITEM_PICKUP(OnPlayerItemPickup.class, void.class),
	ON_PLAYER_ITEM_TRANSFER(OnPlayerItemTransfer.class, void.class),
	// Other player events
	ON_PLAYER_KARMA_CHANGED(OnPlayerKarmaChanged.class, void.class),
	ON_PLAYER_LEVEL_CHANGED(OnPlayerLevelChanged.class, void.class),
	ON_PLAYER_LOGIN(OnPlayerLogin.class, void.class),
	ON_PLAYER_LOGOUT(OnPlayerLogout.class, void.class),
	ON_PLAYER_PK_CHANGED(OnPlayerPKChanged.class, void.class),
	ON_PLAYER_MOVE_REQUEST(OnPlayerMoveRequest.class, void.class, TerminateReturn.class),
	ON_PLAYER_PROFESSION_CHANGE(OnPlayerProfessionChange.class, void.class),
	ON_PLAYER_PROFESSION_CANCEL(OnPlayerProfessionCancel.class, void.class),
	ON_PLAYER_PVP_CHANGED(OnPlayerPvPChanged.class, void.class),
	ON_PLAYER_PVP_KILL(OnPlayerPvPKill.class, void.class),
	ON_PLAYER_RESTORE(OnPlayerRestore.class, void.class),
	ON_PLAYER_SELECT(OnPlayerSelect.class, void.class, TerminateReturn.class),
	ON_PLAYER_SKILL_LEARN(OnPlayerSkillLearn.class, void.class),
	ON_PLAYER_SUMMON_SPAWN(OnPlayerSummonSpawn.class, void.class),
	ON_PLAYER_SUMMON_TALK(OnPlayerSummonTalk.class, void.class),
	ON_PLAYER_TRANSFORM(OnPlayerTransform.class, void.class),
	
	// Trap events
	ON_TRAP_ACTION(OnTrapAction.class, void.class),
	
	// TvT events.
	ON_TVT_EVENT_FINISH(OnTvTEventFinish.class, void.class),
	ON_TVT_EVENT_KILL(OnTvTEventKill.class, void.class),
	ON_TVT_EVENT_REGISTRATION_START(OnTvTEventRegistrationStart.class, void.class),
	ON_TVT_EVENT_START(OnTvTEventStart.class, void.class);
	
	private final Class<? extends IBaseEvent> _eventClass;
	private final Class<?>[] _returnClass;
	
	private EventType(Class<? extends IBaseEvent> eventClass, Class<?>... returnClasss)
	{
		_eventClass = eventClass;
		_returnClass = returnClasss;
	}
	
	public Class<? extends IBaseEvent> getEventClass()
	{
		return _eventClass;
	}
	
	public Class<?>[] getReturnClasses()
	{
		return _returnClass;
	}
	
	public boolean isEventClass(Class<?> clazz)
	{
		return _eventClass == clazz;
	}
	
	public boolean isReturnClass(Class<?> clazz)
	{
		return CommonUtil.contains(_returnClass, clazz);
	}
}
