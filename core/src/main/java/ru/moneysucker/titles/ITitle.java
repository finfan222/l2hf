package ru.moneysucker.titles;

import lombok.Data;

import java.util.function.Consumer;

/**
 * @author finfan
 */
public interface ITitle {

	public abstract boolean test(Object... args);

	public abstract String name();
}
