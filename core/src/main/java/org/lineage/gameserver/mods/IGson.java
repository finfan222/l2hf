package org.lineage.gameserver.mods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author finfan
 */
public interface IGson {
	static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	public default void toJson() {
		GSON.toJson(this);
	}

	public default IGson fromJson(String path) {
		return GSON.fromJson(path, getClass());
	}
}
