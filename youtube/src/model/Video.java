package model;

import java.time.LocalDateTime;

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

	public Video(String name, String location_url, long privacy_id, long user_id) {
		this.name = name;
		this.location_url = location_url;
		this.privacy_id = privacy_id;
		this.date = LocalDateTime.now();
		this.views = 0;
		this.user_id = user_id;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrivacy_id(long privacy_id) {
		this.privacy_id = privacy_id;
	}

	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}

	public void setVideo_id(long video_id) {
		this.video_id = video_id;
	}

}
