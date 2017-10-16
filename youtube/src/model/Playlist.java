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
	private boolean isLoaded;

	private List<Video> videos;

	/**
	 * get from DB , for loading videos use PlaylistDAO -loadVideosInPlaylist
	 */
	Playlist(long playlist_id, String playlist_name, long user_id) {
		this.playlist_id = playlist_id;
		this.playlist_name = playlist_name;
		this.user_id = user_id;
		this.videos = new ArrayList<>();
		isLoaded = false;
	}

	/**
	 * @param name
	 *            - playlist name
	 * @param user_id
	 * @throws PlaylistException
	 *             -not valid name and user
	 */
	public Playlist(String name, long user_id) throws PlaylistException {
		this.playlist_id = (long) 0;
		setName(name);
		setUser_id(user_id);
		this.videos = new ArrayList<>();
		isLoaded = false;
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

	/**
	 * <b>Always use with PlaylistDAO addVideo.Before use check for videos
	 * loaded in playlist</b>
	 * 
	 * @param video
	 * @throws PlaylistException-VIDEOS_NOT_LOADED,VIDEO_AREADY_EXIST
	 * @throws VideoException-INVALID_ID
	 */
	public void addVideo(Video video) throws PlaylistException, VideoException {
		if (video == null || video.getVideo_id() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		if (!isLoaded) {
			throw new PlaylistException(PlaylistException.VIDEOS_NOT_LOADED);
		}
		if (videos.contains(video)) {
			throw new PlaylistException(PlaylistException.VIDEO_AREADY_EXIST);
		}
		//TODO add only public video
		videos.add(video);
	}

	/**
	 * <b>Always use with PlaylistDAO addVideo.Before use check for videos
	 * loaded in playlist</b>
	 * 
	 * @param video
	 * @throws VideoException-INVALID_ID,
	 * @throws VideoNotFoundException NOT_FOUND
	 * @throws PlaylistException VIDEOS_NOT_LOADED
	 */
	public void removeVideo(Video video) throws VideoException, VideoNotFoundException, PlaylistException {
		if (video == null || video.getVideo_id() == 0) {
			throw new VideoException(VideoException.INVALID_ID);
		}
		if (!isLoaded) {
			throw new PlaylistException(PlaylistException.VIDEOS_NOT_LOADED);
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

	public boolean isLoaded() {
		return isLoaded;
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
		return true;
	}

}
