package ru.mmorpg.structure;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author finfan
 */
@NoArgsConstructor
@AllArgsConstructor
public class Bool {

	private boolean value;

	public void reset() {
		value = false;
	}

	public void set(boolean value) {
		this.value = value;
	}

	public void inverse() {
		value = !value;
	}

	public boolean check(boolean expected) {
		return value == expected;
	}

	@Override
	public String toString() {
		return "Bool{" +
			"value=" + value +
			'}';
	}
}
