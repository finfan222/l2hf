package ru.mmorpg.structure.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mmorpg.structure.BitFlag;
import ru.mmorpg.structure.Bool;
import ru.mmorpg.structure.Num;
import ru.mmorpg.structure.Text;

import java.lang.reflect.Field;

/**
 * @author finfan
 */
public interface IClean extends IField {

	static final Logger CLEAN_LOG = LoggerFactory.getLogger(IClean.class);

	default void reset(String var, Object value) {
		try {
			getDeclaredField(var).set(this, value);
		} catch (IllegalAccessException e) {
			CLEAN_LOG.error("Невозможно сделать сброс {} в <{}>{}", var, value.getClass().getTypeName(), value);
		}
	}

	default void clean(String var) {
		try {
			getDeclaredField(var).set(this, null);
		} catch (IllegalAccessException e) {
			CLEAN_LOG.error("Невозможно сделать очистку для {}", var);
		}
	}

	/**
	 * Возвращает все значения объекта в базовое состояние, исключая {@code Null}
	 */
	default void reset() {
		for (Field f : getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.getType().isPrimitive()) {
					if (f.getType() == int.class) {
						f.set(this, 0);
					} else if (f.getType() == float.class) {
						f.set(this, 0.f);
					} else if (f.getType() == double.class) {
						f.set(this, 0.d);
					} else if (f.getType() == short.class) {
						f.set(this, (short) 0);
					} else if (f.getType() == boolean.class) {
						f.set(this, Boolean.FALSE);
					} else if (f.getType() == byte.class) {
						f.set(this, (byte) 0);
					} else if (f.getType() == long.class) {
						f.set(this, 0L);
					} else if (f.getType() == String.class) {
						f.set(this, "");
					}
				} else if (f.getType() == Num.class) {
					f.set(this, new Num(0, 0));
				} else if (f.getType() == Text.class) {
					f.set(this, new Text("", new StringBuilder()));
				} else if (f.getType() == Bool.class) {
					f.set(this, new Bool(false));
				} else if (f.getType() == BitFlag.class) {
					f.set(this, new BitFlag(0));
				} else {
					if (f.getType() == Integer.class) {
						f.set(this, 0);
					} else if (f.getType() == Float.class) {
						f.set(this, 0.f);
					} else if (f.getType() == Double.class) {
						f.set(this, 0.d);
					} else if (f.getType() == Short.class) {
						f.set(this, (short) 0);
					} else if (f.getType() == Boolean.class) {
						f.set(this, Boolean.FALSE);
					} else if (f.getType() == Byte.class) {
						f.set(this, (byte) 0);
					} else if (f.getType() == Long.class) {
						f.set(this, 0L);
					} else if (f.getType() == String.class) {
						f.set(this, "");
					}
				}
			} catch (IllegalAccessException e) {
				CLEAN_LOG.error("Не удалось сбросить переменную {}[{}] класса {} в базовое состояние.",
					f.getName(), f.getType(), getClass().getCanonicalName());
			}
		}
	}

	/**
	 * Возвращает все объекты класса в состояние {@code Null} если это возможно
	 */
	default void clean() {
		for (Field f : getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (!f.getType().isPrimitive() && f.get(this) != null) {
					f.set(this, null);
				}
			} catch (IllegalAccessException e) {
				CLEAN_LOG.error("Не удалось установить переменную {}[{}] класса {} в null!",
					f.getName(), f.getType(), getClass().getCanonicalName());
				return;
			}
		}
	}
}
