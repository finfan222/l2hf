package org.lineage.gameserver.mods.mastery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.lineage.gameserver.mods.IGson;

/**
 * @author finfan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasteryEntity implements IGson {

	private byte level;
	private byte branch;
	private Class<?> penalty;
	private int spConsume;
}
