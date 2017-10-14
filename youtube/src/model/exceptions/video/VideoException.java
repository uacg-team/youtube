package model.exceptions.video;

public class VideoException extends Exception {

	public static final String INVALID_DESCRIPTION = "Invalid video description";
	public static final String INVALID_PRIVACY = "Invalid privacy settings";
	public static final String INVALID_THUMBNAIL = "Invalid thumbnail";
	public static final String NOT_FOUND = "Video not found";

	public VideoException(String message) {
		super(message);
	}
}