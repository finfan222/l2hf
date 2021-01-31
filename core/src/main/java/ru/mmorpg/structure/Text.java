package ru.mmorpg.structure;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author finfan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Text {

	private String text = "";
	private StringBuilder builder;

	/**
	 * Конкатернирует строки используя разделитель {@code delimiter} друг с другом.
	 *
	 * @param delim разделитель вставляющийся после добавления аргумента
	 * @param args аргумент, который конкатенируется
	 * @return {@link Text}
	 */
	public Text append(Character delim, Object... args) {
		for (Object o : args) {
			text += String.valueOf(o);
			if (delim != null) {
				text += delim;
			}
		}
		return this;
	}


	/**
	 * Конкатернирует строки используя разделитель {@code space} друг с другом.
	 *
	 * @param args аргумент, который конкатенируется
	 * @return {@link Text}
	 */
	public Text append(Object... args) {
		return append(' ', args);
	}

	/**
	 * Замена всех слов в тексе разделённых {@code ,} и начинающихся с {@literal $} на значение {@code arg}. Заменяет все возможные позиции со словом
	 * {@code word}
	 *
	 * @return {@link Text}
	 */
	public Text replace(String delimiter, Object... args) {
		int i = 0;
		for (String str : text.split(delimiter)) {
			if (str.startsWith("$")) {
				text = text.replace(str, String.valueOf(args[i++]));
			}
		}
		return this;
	}

	/**
	 * Устанавливает и возвращает {@code text} переменную.
	 *
	 * @param text для ког оустанавливается {@link String}
	 * @return
	 */
	public String set(String text) {
		return this.text = text;
	}

	/**
	 * Создаёт {@link StringBuilder} для атрибута {@code builder} с пустым {@code value}.
	 *
	 * @return {@link StringBuilder}
	 */
	public StringBuilder builder() {
		return this.builder = new StringBuilder();
	}

	/**
	 * Очищает {@code text} и {@code textBuilder} значения присваивая им значения по {@code default}
	 */
	public void clear() {
		text = "";
		builder = null;
	}

	/**
	 * Возвращает {@link Num} {@code array} получаемый из {@link String}
	 *
	 * @param type тип возвращаемого {@link Number}
	 * @param value строка для парса в нумерики
	 * @param delim разделитель использующийся строкой для парса
	 * @return {@link Num} array
	 * @see Num
	 */
	public Num[] asNumArray(Class<? extends Number> type, String value, String delim) {
		String[] result = value.split(delim);
		Num[] array = new Num[result.length];
		for (int i = 0; i < result.length; i++) {
			if (type == Integer.class) {
				final Integer val = Integer.valueOf(result[i]);
				array[i] = new Num(val, val);
			}
			if (type == Double.class) {
				final Double val = Double.valueOf(result[i]);
				array[i] = new Num(val, val);
			}
			if (type == Long.class) {
				final Long val = Long.valueOf(result[i]);
				array[i] = new Num(val, val);
			}
			if (type == Short.class) {
				final Short val = Short.valueOf(result[i]);
				array[i] = new Num(val, val);
			}
			if (type == Byte.class) {
				final Byte val = Byte.valueOf(result[i]);
				array[i] = new Num(val, val);
			}
			if (type == Float.class) {
				final Float val = Float.valueOf(result[i]);
				array[i] = new Num(val, val);
			}
		}
		return array;
	}

	/**
	 * Возвращает {@link Enum} {@code array} получаемый из {@link String}
	 *
	 * @param type тип возвращаемого {@link Enum}
	 * @param value строка для парса в нумерики
	 * @param delim разделитель использующийся строкой для парса
	 * @return {@link Enum} array
	 * @see Enum
	 */
	public Enum[] asEnumArray(Class<? extends Enum> type, String value, String delim) {
		String[] result = value.split(delim);
		Enum[] array = new Enum[result.length];
		for (int i = 0; i < result.length; i++) {
			array[i] = Enum.valueOf(type, result[i]);
		}
		return array;
	}

	/**
	 * Возвращает {@link Integer} из строки в <b>примитивной</b> форме.
	 *
	 * @param value строка откуда парсится {@link Integer}
	 * @return {@link Integer}
	 */
	public int asInt(String value) {
		return Integer.valueOf(value);
	}

	/**
	 * Возвращает {@link Double} из строки в <b>примитивной</b> форме.
	 *
	 * @param value строка откуда парсится {@link Double}
	 * @return {@link Double}
	 */
	public double asDouble(String value) {
		return Double.valueOf(value);
	}

	/**
	 * Возвращает {@link Long} из строки в <b>примитивной</b> форме.
	 *
	 * @param value строка откуда парсится {@link Long}
	 * @return {@link Long}
	 */
	public long asLong(String value) {
		return Long.valueOf(value);
	}

	/**
	 * Возвращает {@link Short} из строки в <b>примитивной</b> форме.
	 *
	 * @param value строка откуда парсится {@link Short}
	 * @return {@link Short}
	 */
	public short asShort(String value) {
		return Short.valueOf(value);
	}

	/**
	 * Возвращает {@link Byte} из строки в <b>примитивной</b> форме.
	 *
	 * @param value строка откуда парсится {@link Byte}
	 * @return {@link Byte}
	 */
	public byte asByte(String value) {
		return Byte.valueOf(value);
	}

	/**
	 * Возвращает {@link Float} из строки в <b>примитивной</b> форме.
	 *
	 * @param value строка откуда парсится {@link Float}
	 * @return {@link Float}
	 */
	public float asFloat(String value) {
		return Float.valueOf(value);
	}

	/**
	 * Вовзращает {@link String} представление {@code hex} значения получаемое от {@link Number}
	 *
	 * @param num
	 * @return {@link String}
	 */
	public String asHex(Number num) {
		return Integer.toHexString(num.intValue());
	}

	@Override
	public String toString() {
		return builder.toString().isEmpty() ? text : builder.toString();
	}
}
