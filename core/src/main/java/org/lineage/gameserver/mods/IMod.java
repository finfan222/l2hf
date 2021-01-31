package org.lineage.gameserver.mods;

/**
 * @author finfan
 */
public interface IMod {
	public void load();

	public void reload();

	public boolean enabled();

}
