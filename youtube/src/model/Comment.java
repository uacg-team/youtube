package model;

import java.time.LocalDateTime;
import java.util.List;

import model.exceptions.comments.CommentException;

public class Comment {
	private long id;
	private String text;
	private LocalDateTime date;
	private List<Comment> replays;
	private long user_id;
	private long video_id;
	private long replayTo_id;

	/**
	 * get all fields
	 */
	public Comment(long id, String text, LocalDateTime date, List<Comment> replays, long user_id, long video_id,
			Long replayTo_id) {
		this.id = id;
		this.text = text;
		this.date = date;
		this.replays = replays;
		this.user_id = user_id;
		this.video_id = video_id;
		this.replayTo_id = replayTo_id;
	}

	/**
	 * register new comment
	 * use replayTo_id=0 to set not replay
	 * @throws CommentException 
	 */
	public Comment(String text, LocalDateTime date, User user, Video video, Comment replayTo) throws CommentException {
		setText(text);
		setDate(date);
		setUser_id(user.getUser_id());
		setVideo_id(video.getVideo_id());
		setReplayTo(replayTo);
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

	public List<Comment> getReplays() {
		return replays;
	}

	public long getUser_id() {
		return user_id;
	}

	public long getVideo_id() {
		return video_id;
	}

	public long getReplayTo_id() {
		return replayTo_id;
	}

	public void setId(long id) throws CommentException {
		if (id < 1) {
			throw new CommentException(CommentException.INVALID_ID);
		}
		this.id = id;
	}

	public void setDate(LocalDateTime date) throws CommentException {
		if (date == null /*|| date.isAfter(LocalDateTime.now().minusMinutes(1))*/) {
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
		this.user_id = user_id;
	}

	public void setVideo_id(long video_id) {
		this.video_id = video_id;
	}
	/**
	 * 
	 * @param replay comment if comment is null replayTo_id=0;
	 * @throws CommentException
	 */
	public void setReplayTo(Comment replay) throws CommentException {
		if(replay==null) {
			this.replayTo_id = 0;
			return;
		}
		this.replayTo_id = replay.getId();
	}
}
