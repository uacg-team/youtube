package model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import model.exceptions.comments.InvalidCommentDateException;
import model.exceptions.comments.InvalidCommentIdException;
import model.exceptions.comments.InvalidCommentTextException;

public class Comment {
	private long id;
	private String text;
	private LocalDateTime date;
	private List<Comment> replays;
	private int user_id;
	private int video_id;
	private Comment replayTo;

	public Comment(long id, String text, LocalDateTime date, int user_id, int video_id, Comment replayTo) {
		// valid
		this.id = id;
		this.text = text;
		this.date = date;
		this.replays = new LinkedList<>();
		this.user_id = user_id;
		this.video_id = video_id;
		this.replayTo = replayTo;
	}

	public Comment() {

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

	public int getUser_id() {
		return user_id;
	}

	public int getVideo_id() {
		return video_id;
	}

	public Comment getReplayTo() {
		return replayTo;
	}

	public void setId(long id) throws InvalidCommentIdException {
		if (id < 1) {
			throw new InvalidCommentIdException();
		}
		this.id = id;
	}

	public void setDate(LocalDateTime date) throws InvalidCommentDateException {
		if (date == null || date.isAfter(LocalDateTime.now().minusMinutes(1))) {
			throw new InvalidCommentDateException();
		}
		this.date = date;
	}

	public void setText(String text) throws InvalidCommentTextException {
		if (text == null) {
			throw new InvalidCommentTextException();
		}
		this.text = text;
	}

	public void setUser_id(int user_id) {
		if (user_id < 1) {
			//TODO throw invalidUserIdException
		}
		this.user_id = user_id;
	}
}
