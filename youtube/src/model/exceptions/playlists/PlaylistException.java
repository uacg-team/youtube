package model.exceptions.playlists;

@SuppressWarnings("serial")
public class PlaylistException extends Exception {
	public static final String INVALID_ID = "playlist id is wrong!";
	public static final String INVALID_USER_ID = "User id is wrong!";
	public static final String INVALID_NAME = "date for comment is wrong!";
	public static final String CANT_UPDATE = "not updated playlist!";
	public static final String MISSING_ID = "playlist have not id";
	public static final String INVALID_NAME_LENGTH_MIN = "name of playlist is too short!";
	public static final String INVALID_NAME_LENGTH_MAX = "name of playlist is too long!";
	public static final String INVALID_NAME_SYMBOLS = "dont use symbols: ; # % | \\ \" < or >";
	public static final String VIDEO_AREADY_EXIST = "video exist in this playlist!";
	public PlaylistException(String message) {
		super(message);
	}
	public PlaylistException() {
	}
}
