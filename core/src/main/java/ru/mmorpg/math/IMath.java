package ru.mmorpg.math;

import ru.mmorpg.structure.Num;

/**
 * @author finfan
 */
public interface IMath {
	public Num mul(Num value);

	public Num div(Num value);

	public Num set(Num value);

	public Num add(Num value);

	public Num sub(Num value);

	public Num mul(double value);

	public Num div(double value);

	public Num set(double value);

	public Num add(double value);

	public Num sub(double value);

	public Num tempMul(double value);

	public Num tempDiv(double value);

	public Num tempAdd(double value);

	public Num tempSub(double value);

	public Num sqrt();

	public Num tempSqrt();

	public Num pow(double to);

	public Num tempPow(double to);
}
