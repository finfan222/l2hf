package ru.mmorpg.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.mmorpg.Todo;
import ru.mmorpg.math.IMath;

import java.util.concurrent.TimeUnit;

/**
 * @author finfan
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Num extends Number implements IMath {

	private Number base = 0, current = 0;

	public Num(Number base) {
		this.base = base;
		this.current = base;
	}

	public Num baseMul(Num value) {
		base = base.doubleValue() * value.doubleValue();
		return this;
	}

	public Num baseAdd(Num value) {
		base = base.doubleValue() + value.doubleValue();
		return this;
	}

	public Num baseSub(Num value) {
		base = base.doubleValue() - value.doubleValue();
		return this;
	}

	public Num baseDiv(Num value) {
		base = base.doubleValue() / value.doubleValue();
		return this;
	}

	public Num baseSet(Num value) {
		base = value.doubleValue();
		return this;
	}

	public Num baseSqrt(Num value) {
		base = Math.sqrt(value.doubleValue());
		return this;
	}

	public Num basePow(double to) {
		base = Math.pow(base.doubleValue(), to);
		return this;
	}

	@Deprecated
	@Todo(comment = "Создание формулы для расчёта Num переменной current")
	public Num calc(String formula) {
		return this;
	}

	@Override
	public int intValue() {
		return current.intValue();
	}

	@Override
	public long longValue() {
		return current.longValue();
	}

	@Override
	public float floatValue() {
		return current.floatValue();
	}

	@Override
	public double doubleValue() {
		return current.doubleValue();
	}

	@Override
	public Num mul(Num value) {
		current = current.doubleValue() * value.doubleValue();
		return this;
	}

	@Override
	public Num div(Num value) {
		current = current.doubleValue() / value.doubleValue();
		return this;
	}

	@Override
	public Num set(Num value) {
		current = value;
		return this;
	}

	@Override
	public Num add(Num value) {
		current = current.doubleValue() + value.doubleValue();
		return this;
	}

	@Override
	public Num sub(Num value) {
		current = current.doubleValue() - value.doubleValue();
		return this;
	}

	@Override
	public Num mul(double value) {
		current = current.doubleValue() * value;
		return this;
	}

	@Override
	public Num div(double value) {
		current = current.doubleValue() / value;
		return this;
	}

	@Override
	public Num set(double value) {
		current = value;
		return this;
	}

	@Override
	public Num add(double value) {
		current = current.doubleValue() + value;
		return this;
	}

	@Override
	public Num sub(double value) {
		current = current.doubleValue() - value;
		return this;
	}

	@Override
	public Num tempMul(double value) {
		double result = current.doubleValue() * value;
		return new Num(result, result);
	}

	@Override
	public Num tempDiv(double value) {
		double result = current.doubleValue() / value;
		return new Num(result, result);
	}

	@Override
	public Num tempAdd(double value) {
		double result = current.doubleValue() + value;
		return new Num(result, result);
	}

	@Override
	public Num tempSub(double value) {
		double result = current.doubleValue() - value;
		return new Num(result, result);
	}

	@Override
	public Num sqrt() {
		current = Math.sqrt(current.doubleValue());
		return this;
	}

	@Override
	public Num tempSqrt() {
		double sqrt = Math.sqrt(current.doubleValue());
		return new Num(sqrt, sqrt);
	}

	@Override
	public Num pow(double to) {
		current = Math.pow(current.doubleValue(), to);
		return this;
	}

	@Override
	public Num tempPow(double to) {
		return null;
	}

	public boolean isOverflowed(double additional) {
		double temp = current.doubleValue() + additional;
		return current.doubleValue() >= temp;
	}

	public boolean isOverflowed() {
		return isOverflowed(0);
	}

	public Number toSeconds() {
		return TimeUnit.MILLISECONDS.toSeconds(current.longValue());
	}

	public Number toMinutes() {
		return TimeUnit.MILLISECONDS.toMinutes(current.longValue());
	}

	/**
	 * @return true если {@code base} && {@code current} равны 0.
	 */
	public boolean isZero() {
		return base.doubleValue() == 0 && current.doubleValue() == 0;
	}
}
