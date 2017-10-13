package model.exceptions.video;

public class VideoNotFoundException extends VideoException {

	public final static String NOT_FOUND = "Video not found";
	
	public VideoNotFoundException(String message) {
		super(message);
	}

}
