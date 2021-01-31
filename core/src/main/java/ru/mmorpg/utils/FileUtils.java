package ru.mmorpg.utils;

import lombok.extern.slf4j.Slf4j;
import ru.mmorpg.structure.Text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author finfan
 */
@Slf4j
public class FileUtils {
	public static final void writeToFile(String path, String str, boolean append) {
		try (FileWriter fw = new FileWriter(path, append); BufferedWriter bw = new BufferedWriter(fw)) {
			bw.append(append ? str.concat("\n") : str);
		} catch (IOException ex) {
			log.error("Error while writing to file: {}", path);
		}
	}

	public static final void writeToFile(String path, Text text, boolean append) {
		writeToFile(path, text.getText(), append);
	}
}
