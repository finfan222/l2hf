package ru.mmorpg.structure.enums;

/**
 * @author finfan
 */
public interface Enumerate {
	/**
	 * @param enumName имя перечилсяемого значения (всегда возводится в UpperCase)
	 * @return перечисляемое значение наследуемое от {@link Enumerate} по его имени
	 */
	public Enum get(String enumName);

	/**
	 * @return Возвращает массив перечисляемых значений
	 */
	public Enum[] array();

	/**
	 * @return Вовзаращет нормальное имя перечислямого значения, т.е. без всех '_' символов и в нормальном case формате
	 */
	public String title();

	/**
	 * @return ID перечисляемого значения, начинается с 1
	 */
	public int id();

	/**
	 * @return ordinal перечисляемого значения, начинается с 0
	 */
	public int order();

	/**
	 * @return Маска перечисляемого значения, которое берётся либо из order() либо из personal() (если оно стоит), в приоритет - personal()
	 */
	default int mask() {
		// 01 << 16 = 010010101011101010
		return personal() > 0 ? personal() : 1 << order();
	}

	/**
	 * Персональная маска для значения. Устанавливается в самом значении enum как его аргумент.
	 * @return {@link Integer} перечисляемого значения
	 */
	default int personal() {
		return 0;
	}
}
