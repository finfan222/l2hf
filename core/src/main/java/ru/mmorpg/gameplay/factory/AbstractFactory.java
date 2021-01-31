package ru.mmorpg.gameplay.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author finfan
 */
public abstract class AbstractFactory {

	protected static final Logger log = LoggerFactory.getLogger(AbstractFactory.class);

	protected final Map<Class<?>, Set<Object>> createdInstances = new HashMap<>();

	public abstract <T> T create(Object... args);

	public abstract void destroy(Object... args);

	public final void info(Class<?> type) {
		log.info("Созданных инстанций {}: {} штук.", type.getSimpleName(), createdInstances.size());
	}
}
