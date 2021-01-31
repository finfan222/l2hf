package ru.mmorpg;

/**
 * @author finfan
 */
public interface Component {
	void constructor(Object... args);
	void destructor(Object... args);
}
