package ru.mmorpg.utils;

/**
 * @author finfan
 */
public class TextUtils {

	/*
	Console Text Colors
	 */
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	/**
	 * Вставляет текст в консоль через {@code System.out.println()}
	 *
	 * @param text вставляемая строка
	 * @param ansiColor цвет строки берётся из {@link TextUtils} и начинается с {@code ANSI}
	 */
	public static void insertColoredText(String text, String ansiColor) {
		System.out.println(org.apache.commons.lang3.StringUtils.center(ansiColor + text + ansiColor, text.length()));
	}

	/**
	 * Нормализация {@code text} в читаемый формат.<br>
	 * <ul>
	 *     <li>Каждый символ начинающийся после ' ' даёт нам Возвышение в UpperCase.</li>
	 *     <li>Последующие символы дают нам LowerCase</li>
	 * </ul>
	 * <br>
	 * <b>Пример</b>: ENUM_TEST_VALUE -> normalize(ENUM_TEST_VALUE) = Enum test value
	 */
	public static String normalize(Enum e, boolean everyToUpper) {
		final char[] chars = e.name().toCharArray();
		boolean toUpper = true;
		for (int i = 0; i < chars.length; i++) {
			Character ch = chars[i];
			if (toUpper) {
				ch = Character.toUpperCase(ch);
				toUpper = false;
			} else if(everyToUpper && ch == '_') {
				ch = ' ';
				toUpper = true;
			}
			chars[i] = ch;
		}
		return String.valueOf(chars);
	}

	/**
	 *
	 * @param e
	 * @return
	 */
	public static String normalize(Enum e) {
		return normalize(e, true);
	}
}
