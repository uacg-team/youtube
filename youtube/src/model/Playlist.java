package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;
import model.exceptions.video.VideoException;
import model.exceptions.video.VideoNotFoundException;

public class Playlist {
	private static final int MIN_LENGTH_PLAYLIST_NAME = 3;
	private static final int MAX_LENGTH_PLAYLIST_NAME = 45;
	private long playlist_id;
	private String playlist_name;
	private long user_id;

	private List<Video> videos;

	/**
	 * get from DB , for loading videos use PlaylistDAO -loadVideosInPlaylist
	 */
	public Playlist(long playlist_id, String playlist_name, long user_id) {
		this.playlist_id = playlist_id;
		this.playlist_name = playlist_name;
		this.user_id = user_id;
		this.videos = new ArrayList<>();
	}

	/**
	 * @param name
	 *            - new playlist name
	 * @param user
	 *            -user with id
	 * @throws PlaylistException
	 *             -not valid name and user
	 */
	public Playlist(String name, User user) throws PlaylistException {
		this.playlist_id = (long) 0;
		setName(name);
		setUser_id(user_id);
		this.videos = new ArrayList<>();
	}

	public long getId() {
		return playlist_id;
	}

	public String getName() {
		return playlist_name;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setId(long playlist_id) throws PlaylistException {
		if (playlist_id < 1) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		this.playlist_id = playlist_id;
	}

	public void setName(String playlist_name) throws PlaylistException {
		if (playlist_name == null || playlist_name.isEmpty()) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		if (playlist_name.length() < MIN_LENGTH_PLAYLIST_NAME) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_LENGTH_MIN);
		}
		if (playlist_name.length() >= MAX_LENGTH_PLAYLIST_NAME) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_LENGTH_MAX);
		}
		if (!Pattern.matches("[^;#/%|\\\\\"<>]+", playlist_name)) {
			throw new PlaylistException(PlaylistException.INVALID_NAME_SYMBOLS);
		}
		this.playlist_name = playlist_name;
	}

	public void setUser_id(long user_id) throws PlaylistException {
		if (user_id < 1) {
			throw new PlaylistException(UserException.INVALID_ID);
		}
		this.user_id = user_id;
	}

	public void addVideo(Video video) throws PlaylistException, VideoException {
		if (video == null || video.getVideo_id() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		if (videos.contains(video)) {
			throw new PlaylistException(PlaylistException.VIDEO_AREADY_EXIST);
		}
		videos.add(video);
	}

	public void removeVideo(Video video) throws VideoException {
		if (video == null || video.getVideo_id() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		if (videos.contains(video)) {
			throw new VideoNotFoundException(VideoNotFoundException.NOT_FOUND);
		}
		videos.remove(video);
	}
	/**
	 * @return unmodifiableList(videos)
	 */
	public List<Video> getVideos() {
		return Collections.unmodifiableList(videos);
	}

	void setVideos(List<Video> videos) {
		this.videos = videos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Playlist))
			return false;
		Playlist other = (Playlist) obj;
		if (playlist_id != other.playlist_id)
			return false;
		if (playlist_name == null) {
			if (other.playlist_name != null)
				return false;
		} else if (!playlist_name.equals(other.playlist_name))
			return false;
		if (user_id != other.user_id)
			return false;
		if (videos == null) {
			if (other.videos != null)
				return false;
		} else if (!videos.equals(other.videos))
			return false;
		return true;
	}

}
