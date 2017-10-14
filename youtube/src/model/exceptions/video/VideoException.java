package model.exceptions.video;

@SuppressWarnings("serial")
public class VideoException extends Exception {

	public static final String INVALID_DESCRIPTION = "Invalid video description";
	public static final String INVALID_PRIVACY = "Invalid privacy settings";
	public static final String INVALID_THUMBNAIL = "Invalid thumbnail";
	public static final String NOT_FOUND = "Video not found";
	public static final String INVALID_ID = "Invalid user id";
	
	public VideoException(String message) {
		super(message);
	}
}
