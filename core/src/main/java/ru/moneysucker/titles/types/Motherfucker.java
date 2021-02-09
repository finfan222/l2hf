package ru.moneysucker.titles.types;

import org.lineage.gameserver.model.actor.instance.PlayerInstance;
import org.lineage.gameserver.model.events.EventType;
import org.lineage.gameserver.model.events.impl.creature.player.OnPlayerPvPKill;
import org.lineage.gameserver.model.events.listeners.AbstractEventListener;
import org.lineage.loginserver.network.gameserverpackets.PlayerInGame;
import ru.moneysucker.titles.ITitle;

import java.util.Queue;

/**
 * Если PVP > 10 и смертей 0
 * @author finfan
 */
public class Motherfucker implements ITitle {

	@Override
	public boolean test(Object... args) {
		final PlayerInstance player = (PlayerInstance) args[0];

		final AbstractEventListener onPvPKill = player.getListener(EventType.ON_PLAYER_PVP_KILL, Motherfucker.class);

		final int kills = player.getVariables().getInt("kills");
		final int deaths = player.getVariables().getInt("deaths");
		if (kills >= 10 && deaths == 0) {
			return true;
		}
		return false;
	}

	@Override
	public String name() {
		return "Мамкин Нагибатор";
	}
}
