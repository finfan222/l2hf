package ru.mmorpg.structure.interfaces;

/**
 * @author finfan
 */
public interface ICrudRepository<Id, Type> {
	public void create();
	public void restore();
	public void update();
	public void delete();
}
