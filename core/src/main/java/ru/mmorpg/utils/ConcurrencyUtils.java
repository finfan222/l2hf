package ru.mmorpg.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author finfan
 */
@Slf4j
public class ConcurrencyUtils {
	public static boolean callLock(ReentrantLock locker, Runnable runnable, Runnable onException) {
		locker.lock();
		try {
			runnable.run();
		} catch (Exception e) {
			onException.run();
			log.info("", e);
			return false;
		} finally {
			locker.unlock();
		}
		return true;
	}
}
