package model.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.servlet.http.HttpServletResponse;

public abstract class Resources {
	public static final String ROOT = "C:" + File.separator + "res";
	/**
	 * Example: C:\\Users\\YouTube-PNG-Photos.png
	 * @param absolutePath
	 * @param response
	 * @throws IOException
	 */
	public static void readFromFile(String absolutePath, HttpServletResponse response) throws IOException {
		File myFile = new File(absolutePath);
		try (OutputStream out = response.getOutputStream()) {
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		}
	}

	/**
	 * @param absolutePath:
	 *            C:\\Users\\YouTube-PNG-Photos.png
	 * @param inStream
	 *            -
	 * @throws IOException
	 */
	public static void writeFile(String absolutePath, InputStream inStream) throws IOException {
		File myFile = new File(absolutePath);
		if (!myFile.exists()) {
			System.out.println(absolutePath);
			myFile.createNewFile();
		}
		Files.copy(inStream, myFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
	}
}
