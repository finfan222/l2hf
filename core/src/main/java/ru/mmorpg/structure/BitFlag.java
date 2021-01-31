package ru.mmorpg.structure;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.mmorpg.structure.enums.Enumerate;
import ru.mmorpg.structure.interfaces.IClean;

/**
 * @author finfan
 */
@NoArgsConstructor
@AllArgsConstructor
public class BitFlag implements IClean {
	private int value;

	public int or(int value) {
		return this.value |= value;
	}

	public int or(Enumerate e) {
		return or(e.mask());
	}

	public boolean and(int checked) {
		return (this.value & checked) == checked;
	}

	public boolean and(Enumerate e) {
		return (this.value & e.mask()) == e.mask();
	}
}
