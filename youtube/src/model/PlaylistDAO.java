package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;
import model.utils.DBConnection;

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
	//not tested
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
	//not tested
	/**
	 * @param playlist
	 *            - must contain playlist_id
	 * @param updateVideosInDB
	 *            -if true <b>delete all videos for this playlist and write all
	 *            videos loaded in Playlist videos collection</b>,else update
	 *            only playlist name and user_id
	 * @throws PlaylistException
	 *             - playlist id is default
	 * @throws SQLException
	 */
	public void updatePlaylist(Playlist playlist, boolean updateVideosInDB) throws PlaylistException, SQLException {
		if (playlist.getUser_id() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		// update playlist info
		String sql = "update playlists set playlist_name=?,user_id=? where playlist_id=?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, playlist.getName());
		ps.setLong(2, playlist.getUser_id());
		ps.setLong(3, playlist.getId());
		ps.executeUpdate();
		ps.close();
		// update playlist videos
		if (updateVideosInDB) {
			deleteVideosInPlaylistDB(playlist);
			sql = "insert into playlists_has_videos (playlist_id,video_id) values (?,?);";
			List<Video> videos = playlist.getVideos();
			for (Video v : videos) {
				ps = con.prepareStatement(sql);
				ps.setLong(1, playlist.getId());
				ps.setLong(2, v.getVideo_id());
				ps.executeUpdate();
				ps.close();
			}
		}
	}

	/**
	 * @param playlist
	 *            must have playlist_id
	 * @throws PlaylistException
	 *             - if playlist have no id
	 * @throws SQLException
	 */
	public void deletePlaylist(Playlist playlist) throws PlaylistException, SQLException {
		// delete videos in playlist
		deleteVideosInPlaylistDB(playlist);
		// delete playlist
		String sql = "delete from playlists where playlist_id=?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, playlist.getId());
		ps.executeUpdate();
		ps.close();
	}
	//not tested
	/**
	 * @param user must have user_id
	 * @return arraylist with playlists without loaded videos in it
	 * @throws UserException if user_id missing
	 * @throws SQLException
	 */
	public List<Playlist> getPlaylists(User user) throws UserException, SQLException {
		if (user.getUser_id() == 0) {
			throw new UserException(UserException.INVALID_ID);
		}
		List<Playlist> playlists = new ArrayList<>();
		String sql = "select * from playlists where user_id=?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, user.getUser_id());
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			long playlist_id = rs.getLong("playlist_id");
			String playlist_name = rs.getString("playlist_name");
			Playlist playlist = new Playlist(playlist_id, playlist_name, user.getUser_id());
			playlists.add(playlist);
		}
		return playlists;
	}
	//not tested
	/**
	 * @param user -must have id
	 * @param playlistName not null and not empty;
	 * @return playlist if exist playlist for user with playlistName ignoreCase or null;
	 * @throws UserException - if user have no id
	 * @throws SQLException
	 * @throws PlaylistException -if playlistName is null or empty
	 */
	public Playlist getPlaylist(User user, String playlistName) throws UserException, SQLException, PlaylistException {
		if(playlistName==null || playlistName.isEmpty()) {
			throw new PlaylistException(PlaylistException.INVALID_NAME);
		}
		List<Playlist> playslist = getPlaylists(user);
		for(Playlist p :playslist) {
			if(p.getName().equalsIgnoreCase(playlistName)) {
				return p;
			}
		}
		return null;
	}

	public void loadVideosInPlaylist(Playlist playlist) {
		// TODO
	}

	/**
	 * @param playlist
	 *            must have playlist_id
	 * @throws PlaylistException
	 * @throws SQLException
	 */
	private void deleteVideosInPlaylistDB(Playlist playlist) throws PlaylistException, SQLException {
		if (playlist.getUser_id() == 0) {
			throw new PlaylistException(PlaylistException.INVALID_ID);
		}
		String sql = "delete from playlists_has_videos where playlist_id = ?;";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setLong(1, playlist.getId());
		ps.executeUpdate();
	}

	// Test
	public static void main(String[] args) {
		Playlist p = new Playlist(1, "ala bala pleylist", 1);
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
