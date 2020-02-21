package frc.team5104.util;

import java.io.File;
import java.io.IOException;

public class Filer {
	public static boolean fileExists(String fullPath) {
		File file = new File(fullPath);
		return file.exists();
	}
	
	public static void createFile(String fullPath) {
		File file = new File(fullPath);
		try {
			file.createNewFile();
		} catch (IOException e) { }
	}
}
