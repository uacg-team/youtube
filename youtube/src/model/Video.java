package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.exceptions.user.UserException;
import model.exceptions.video.VideoException;

/**
 * VIDEO POJO Class
 * 
 * @author HP
 *
 */
public class Video {

	private static final int MIN_NAME_LENGTH = 3;
	private long video_id;
	private String name;
	private int views;
	private LocalDateTime date;
	private String location_url;
	private long user_id;
	private String thumbnail_url;
	private String description;
	private long privacy_id;

	private List<Tag> tags = new ArrayList<>();

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
	Video(long video_id, String name, int views, LocalDateTime date, String location_url, long user_id,
			String thumbnail_url, String description, long privacy_id, List<Tag> tags) {
		this.video_id = video_id;
		this.name = name;
		this.views = views;
		this.date = date;
		this.location_url = location_url;
		this.user_id = user_id;
		this.thumbnail_url = thumbnail_url;
		this.description = description;
		this.privacy_id = privacy_id;
		this.tags = tags;
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
	 * @throws VideoException
	 */
	public Video(String name, String location_url, long privacy_id, long user_id, List<Tag> tags)
			throws VideoException {
		setName(name);
		setLocation_url(location_url);
		setPrivacy_id(privacy_id);
		setUser_id(user_id);
		setTags(tags);

		this.date = LocalDateTime.now();
		this.views = 0;
	}

	public void addTag(Tag t) {
		this.tags.add(t);
	}

	public LocalDateTime getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public String getLocation_url() {
		return this.location_url;
	}

	public String getName() {
		return this.name;
	}

	public long getPrivacy_id() {
		return this.privacy_id;
	}

	public List<Tag> getTags() {
		return this.tags;
	}

	public String getThumbnail_url() {
		return this.thumbnail_url;
	}

	public long getUser_id() {
		return this.user_id;
	}

	public long getVideo_id() {
		return this.video_id;
	}

	public int getViews() {
		return this.views;
	}

	public void removeTag(Tag t) {
		this.tags.remove(t);
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setDescription(String description) throws VideoException {
		if (description == null || description.isEmpty()) {
			throw new VideoException(VideoException.INVALID_DESCRIPTION);
		}
		this.description = description;
	}

	public void setLocation_url(String location_url) throws VideoException {
		if (name == null || name.isEmpty()) {
			throw new VideoException(VideoException.INVALID_LOCATION);
		}
		this.location_url = location_url;
	}

	public void setName(String name) throws VideoException {
		if (name == null || name.isEmpty()) {
			throw new VideoException(VideoException.INVALID_NAME);
		}
		if (name.length() < MIN_NAME_LENGTH) {
			throw new VideoException(VideoException.INVALID_NAME_LENGTH);
		}
		this.name = name;
	}
	
	public void setPrivacy_id(long privacy_id) throws VideoException {
		if (privacy_id < 1) {
			throw new VideoException(VideoException.INVALID_PRIVACY);
		}
		this.privacy_id = privacy_id;
	}
	
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void setThumbnail_url(String thumbnail_url) throws VideoException {
		if (thumbnail_url == null || thumbnail_url.isEmpty()) {
			throw new VideoException(VideoException.INVALID_THUMBNAIL);
		}
		this.thumbnail_url = thumbnail_url;
	}

	public void setUser_id(long user_id) throws VideoException {
		if (user_id < 1) {
			throw new VideoException(UserException.INVALID_ID);
		}
		this.user_id = user_id;
	}

	public void setVideo_id(long video_id) {
		this.video_id = video_id;
	}

	public void setViews(int views) {
		this.views = views;
	}

	@Override
	public String toString() {
		return "Video [video_id=" + video_id + ", name=" + name + ", views=" + views + ", date=" + date
				+ ", location_url=" + location_url + ", user_id=" + user_id + ", thumbnail_url=" + thumbnail_url
				+ ", description=" + description + ", privacy_id=" + privacy_id + ", tags=" + tags + "]";
	}

}
