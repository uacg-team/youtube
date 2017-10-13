package model.exceptions.comments;

/**
 * for not valid Comment
 */
public class CommentException extends Exception {
	public static final String INVALID_ID = "comment id is wrong!";
	public static final String INVALID_COMMENT_REPLAY_ID = "comment replay id is wrong!";
	public static final String INVALID_DATE = "date for comment is wrong!";
	public static final String INVALID_TEXT = "text of comment is wrong!";
	public static final String CANT_UPDATE = "not updated comment!";
	
	private static final long serialVersionUID = 1L;

	public CommentException(String msg) {
		super(msg);
	}

	public CommentException() {
	}
}
