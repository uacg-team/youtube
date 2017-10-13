package model;

import java.time.LocalDateTime;
import java.util.HashSet;

import model.exceptions.video.VideoException;

public class Video {

	private long video_id;
	private String name;
	private int views;
	private LocalDateTime date;
	private String location_url;
	private long user_id;
	private String thumbnail_url;
	private String description;
	private long privacy_id;
	private HashSet<Tag> tags = new HashSet<>();

	/**
	 * Use this contructor when get video from database
	 * 
	 * @param video_id
	 *            - video id;
	 * @param name
	 *            - video name;
	 * @param views
	 *            - number of views
	 * @param date
	 *            - date published
	 * @param location_url
	 *            - location
	 * @param user_id
	 *            - owner of the video
	 * @param thumbnail_url
	 *            - thumbnail location
	 * @param description
	 *            - video description
	 * @param privacy_id
	 *            - video privacy settings
	 * @param tags
	 *            - video tags
	 */
	public Video(long video_id, String name, int views, LocalDateTime date, String location_url, long user_id,
			String thumbnail_url, String description, long privacy_id) {
		this.video_id = video_id;
		this.name = name;
		this.views = views;
		this.date = date;
		this.location_url = location_url;
		this.user_id = user_id;
		this.thumbnail_url = thumbnail_url;
		this.description = description;
		this.privacy_id = privacy_id;
	}

	
	/**
	 * Use this constructor when uploading new video Creating video
	 * 
	 * @param name
	 *            - video name
	 * @param location_url
	 *            - video location
	 * @param privacy_id
	 *            - video privacy settings
	 * @param user_id
	 *            - user who create the video
	 * @param tags
	 *            - video tags
	 */
	public Video(String name, String location_url, long privacy_id, long user_id, HashSet<Tag> tags) {
		this.name = name;
		this.location_url = location_url;
		this.privacy_id = privacy_id;
		this.user_id = user_id;
		this.tags = tags;

		this.date = LocalDateTime.now();
		this.views = 0;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}

	public String getLocation_url() {
		return location_url;
	}

	public String getName() {
		return name;
	}

	public long getPrivacy_id() {
		return privacy_id;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public long getUser_id() {
		return user_id;
	}

	public long getVideo_id() {
		return video_id;
	}

	public int getViews() {
		return views;
	}

	public void setDescription(String description) throws VideoException {
		if (description == null || description.isEmpty()) {
			throw new VideoException(VideoException.INVALID_DESCRIPTION);
		}
		this.description = description;
	}

	public void setPrivacy_id(long privacy_id) throws VideoException {
		if (privacy_id < 1) {
			throw new VideoException(VideoException.INVALID_PRIVACY);
		}
		this.privacy_id = privacy_id;
	}

	public void setThumbnail_url(String thumbnail_url) throws VideoException {
		if (thumbnail_url == null || thumbnail_url.isEmpty()) {
			throw new VideoException(VideoException.INVALID_THUMBNAIL);
		}
		this.thumbnail_url = thumbnail_url;
	}

	public void setVideo_id(long video_id) {
		this.video_id = video_id;
	}

}
