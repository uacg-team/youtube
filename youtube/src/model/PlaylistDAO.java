package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;
import model.utils.DBConnection;
import model.utils.DateTimeConvertor;

public class PlaylistDAO {
	private static final Connection con = DBConnection.CON1.getConnection();
	private static PlaylistDAO instance;
	static {
		instance = new PlaylistDAO();
	}

	private PlaylistDAO() {
	}

	public static PlaylistDAO getInstance() {
		return instance;
	}

	// not tested
	/**
	 * @param playlist
	 *            must contain user_id, and Playlist name
	 * @throws PlaylistException
	 *             -invalid user id or name
	 * @throws SQLException
	 */
	public void createPlaylist(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getUser_id() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_USER_ID);
		}
		if (playlist.getName() == null) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		// TODO check for playlist with this name for user!If exist throw new
		// Exception
		String sql = "insert into playlists (user_id,playlist_name) values (?,?);";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, playlist.getUser_id());
		ps.setString(2, playlist.getName());
		ps.executeUpdate();
		ps.close();
	}

	// not tested
	/**
	 * Update only name for playlist
	 * 
	 * @param playlist
	 *            - must contain playlist_id
	 * @throws PlaylistException
	 *             - playlist id is default
	 * @throws SQLException
	 */
	public void updatePlaylist(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getUser_id() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		// update playlist info
		String sql = "update playlists set playlist_name=?,user_id=? where playlist_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, playlist.getName());
			ps.setLong(2, playlist.getUser_id());
			ps.setLong(3, playlist.getId());
			ps.executeUpdate();
		}
	}

	/**
	 * @param playlist_id
	 * @throws PlaylistException
	 *             - if playlist have no id
	 * @throws SQLException
	 */
	public void deletePlaylist(long playlist_id) throws PlaylistException, SQLException {
		// delete videos in playlist
		deleteVideosInPlaylistDB(playlist_id);
		// delete playlist
		String sql = "delete from playlists where playlist_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlist_id);
			ps.executeUpdate();
		}
	}

	// not tested
	/**
	 * @param user_id
	 * @return arraylist with playlists without loaded videos in it
	 * @throws UserException
	 *             if user_id missing
	 * @throws SQLException
	 */
	public List<Playlist> getPlaylists(long user_id) throws UserException, SQLException {
		List<Playlist> playlists = new ArrayList<>();
		String sql = "select * from playlists where user_id=?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, user_id);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long playlist_id = rs.getLong("playlist_id");
					String playlist_name = rs.getString("playlist_name");
					Playlist playlist = new Playlist(playlist_id, playlist_name, user_id);
					playlists.add(playlist);
				}
				return playlists;
			}
		}
	}

	// not tested
	/**
	 * @param user_id
	 * @param playlistName
	 *            not null and not empty;
	 * @return playlist if exist playlist for user with playlistName ignoreCase
	 *         or null;
	 * @throws UserException
	 *             - if user have no id
	 * @throws SQLException
	 * @throws PlaylistException
	 *             -if playlistName is null or empty
	 */
	public Playlist getPlaylist(long user_id, String playlistName)
			throws UserException, SQLException, PlaylistException {
		if (playlistName == null || playlistName.isEmpty()) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		List<Playlist> playslist = getPlaylists(user_id);
		for (Playlist p : playslist) {
			if (p.getName().equalsIgnoreCase(playlistName)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @param playlist
	 *            must contain real id,load videos in this playlist
	 * @throws PlaylistException
	 *             - if playlist_id is 0
	 * @throws SQLException
	 */
	public void loadVideosInPlaylist(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getId() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		String sql = "select * from videos as v inner join "
				+ "(select * from playlists_has_videos where playlist_id=?) as p" + " on(v.video_id=p.video_id);";
		List<Video> videos = new ArrayList<>();
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlist.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					long video_id = rs.getLong("video_id");
					String name = rs.getString("name");
					int views = rs.getInt("views");
					LocalDateTime date = DateTimeConvertor.sqlToLdt(rs.getString("date"));
					String location_url = rs.getString("location_url");
					long user_id = rs.getLong("user_id");
					String thumbnail_url = rs.getString("thumbnail_url");
					String description = rs.getString("description");
					long privacy_id = rs.getLong("privacy_id");
					// tags for video, not loaded!
					Video video = new Video(video_id, name, views, date, location_url, user_id, thumbnail_url,
							description, privacy_id, null);
					videos.add(video);
				}
				playlist.setVideos(videos);
			}
		}
	}

	/**
	 * @param playlist_id
	 * @throws PlaylistException
	 * @throws SQLException
	 */
	private void deleteVideosInPlaylistDB(long playlist_id) throws PlaylistException, SQLException {
		String sql = "delete from playlists_has_videos where playlist_id = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, playlist_id);
			ps.executeUpdate();
		}
	}

	public void addVideo(Playlist playlist, Video video) {
		// TODO add video in DB for this playlist
		// Add only public video
	}

	public void removeVideo(Playlist playlist, Video video) {
		// TODO add video in DB for this playlist
	}

	public List<Playlist> getPlaylists() {
		// TODO
		return null;
	}

	public List<Playlist> searchPlaylist(String search) {
		// TODO ignore case in db

		// String sql = "select * from playlists where playlists.`playlist_name`
		// COLLATE UTF8_GENERAL_CI LIKE '%?%'";
		return null;
	}

	// Test
	public static void main(String[] args) {
		// Playlist p = new Playlist(1, "ala bala pleylist", 1);
		/* create test */
		// try {
		// PlaylistDAO.getInstance().createPlaylist(p);
		// } catch (PlaylistException | SQLException e) {
		// e.printStackTrace();
		// }
		/* delete videos from playlist */
		// try {
		// PlaylistDAO.getInstance().deleteVideosInPlaylistDB(p);
		// } catch (PlaylistException | SQLException e) {
		// e.printStackTrace();
		// }
		try {
			DBConnection.CON1.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
