package ru.mmorpg.structure.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author finfan
 */
public interface IField {

	static final Logger FIELD_LOG = LoggerFactory.getLogger(IField.class);

	default Field[] getDeclaredFields() {
		return getClass().getDeclaredFields();
	}

	default Field getDeclaredField(String name) {
		try {
			return getClass().getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			FIELD_LOG.error("Ошибка в установлении поля {} т.к. такого поля не существует в классе {}", name, getClass().getSimpleName(), e);
			return null;
		}
	}

	default Field getDeclaredField(int index) {
		final Field[] declaredFields = getDeclaredFields();
		try {
			return declaredFields[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			final boolean returnLast = index > declaredFields.length - 1;
			index = declaredFields.length - 1;
			return returnLast ? declaredFields[index] : declaredFields[0];
		}
	}

	default <T> void set(String var, T value) {
		try {
			getDeclaredField(var).set(this, value);
		} catch (IllegalAccessException e) {
			FIELD_LOG.error("Ошибка в установлении поля {} значением {}", var, value, e);
		}
	}

	default <T> void set(int index, T value) {
		try {
			getDeclaredField(index).set(this, value);
		} catch (IllegalAccessException e) {
			FIELD_LOG.error("Ошибка в установлении поля с индексом {} значением {}", index, value, e);
		}
	}
}
