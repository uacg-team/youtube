package model;

import java.time.LocalDateTime;
import java.util.List;

import model.exceptions.comments.CommentException;

public class Comment {
	private long id;
	private String text;
	private LocalDateTime date;
	private long user_id;
	private long video_id;
	private long replay_id;

	private List<Comment> replays;
	private boolean hasReplays;
	/**
	 * get all fields default, use only by CommentDAO
	 */
	Comment(long id, String text, LocalDateTime date, long user_id, long video_id, Long replay_id) {
		this.id = id;
		this.text = text;
		this.date = date;
		this.user_id = user_id;
		this.video_id = video_id;
		this.replay_id = replay_id;
	}

	/**
	 * register new comment use replayTo_id=0 to set no replay
	 * 
	 * @throws CommentException
	 */
	public Comment(String text, LocalDateTime date, long user_id, long video_id, long replay_id)
			throws CommentException {
		setText(text);
		setDate(date);
		setUser_id(user_id);
		setVideo_id(video_id);
		setReplay_id(replay_id);
	}

	public long getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public long getUser_id() {
		return user_id;
	}

	public long getVideo_id() {
		return video_id;
	}

	public long getReplayTo_id() {
		return replay_id;
	}

	public void setId(long id) throws CommentException {
		if (id < 1) {
			throw new CommentException(CommentException.INVALID_ID);
		}
		this.id = id;
	}

	public void setDate(LocalDateTime date) throws CommentException {
		if (date == null) {
			throw new CommentException(CommentException.INVALID_DATE);
		}
		this.date = date;
	}

	public void setText(String text) throws CommentException {
		if (text == null) {
			throw new CommentException(CommentException.INVALID_TEXT);
		}
		this.text = text;
	}

	public void setUser_id(long user_id) {
		// user_id validate in other place
		this.user_id = user_id;
	}

	public void setVideo_id(long video_id) {
		// video_id validate in other place
		this.video_id = video_id;
	}

	public void setReplay_id(long replay_id) {
		this.replay_id = replay_id;
	}

	public void addReplays(List<Comment> allReplays) {
		this.replays = allReplays;
		this.hasReplays = true;
	}

	public List<Comment> getReplays() {
		return replays;
	}
	public boolean getHasReplays() {
		return hasReplays;
	}
}
