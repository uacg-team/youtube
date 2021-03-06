package model.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.User;
import model.UserDao;
import model.exceptions.user.UserException;
import model.exceptions.user.UserNotFoundException;

public abstract class Resources {
	public static final String ROOT = "C:" + File.separator + "res";
	public static final String VIDEO_URL = "videos";
	public static final String IMAGE_URL = "images";
	
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

	public static void writeAvatar(User u, Part image) throws IOException, SQLException, UserException, UserNotFoundException {
		String fileName = Paths.get(image.getSubmittedFileName()).getFileName().toString();
		String absolutePath = Resources.ROOT + File.separator + u.getUserId()+ File.separator + Resources.IMAGE_URL+ File.separator + fileName;
		System.out.println("Resources:writeImage:absolutePath:"+absolutePath);
		write(image, absolutePath);
	}
	
	public static void writeVideo(User u, Part file) throws IOException {
		String fileName = Paths.get(file.getSubmittedFileName()).getFileName().toString();
		String absolutePath = Resources.ROOT + File.separator + u.getUserId()+ File.separator + Resources.VIDEO_URL+ File.separator + fileName;
		System.out.println("Resources:writeVideo:absolutePath:"+absolutePath);
		write(file, absolutePath);
	}
	
	private static void write(Part file, String absolutePath) throws IOException {
		InputStream inputStream = file.getInputStream();
		File myFile = new File(absolutePath);
		if (!myFile.exists()) {
			myFile.mkdirs();
			myFile.createNewFile();
		}
		Files.copy(inputStream, myFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
		inputStream.close();
	}
	
	private static void read(String absolutePath, Long userId, HttpServletResponse response) throws IOException {
		File myFile = new File(absolutePath);
		try (OutputStream out = response.getOutputStream()) {
			Path path = myFile.toPath();
			Files.copy(path, out);
			out.flush();
		}
	}
	
	public static void readAvatar(String filename, Long userId, HttpServletResponse response) throws IOException {
		String absolutePath = Resources.ROOT + File.separator + userId + File.separator + Resources.IMAGE_URL
				+ File.separator + filename;
		System.out.println("Resources:readImage:absolutePath:"+absolutePath);
		read(absolutePath, userId, response);
	}

	public static void readVideo(String url, Long userId, HttpServletResponse response) throws IOException {
		String absolutePath = Resources.ROOT + File.separator + userId + File.separator + Resources.VIDEO_URL+ File.separator + url;
		System.out.println("Resources:readVideo:absolutePath:"+absolutePath);
		read(absolutePath, userId, response);
	}
}
