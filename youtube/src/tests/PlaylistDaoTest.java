package tests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import model.Playlist;
import model.PlaylistDao;
import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;
import model.utils.DBConnection;

public class PlaylistDaoTest {
	private static final Connection con = DBConnection.CON1.getConnection();

	@Test
	public void testCreatePlaylist() throws PlaylistException, SQLException, UserException {
		// creating 10 playlist for user_id=j;
		for (int j = 1; j <= 2; j++) {
			for (int i = 0; i < 10; i++) {
				PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + i, j));
			}
		}
	}

	@Test(expected = PlaylistException.class)
	public void testCreatePlaylistWithSameNames() throws PlaylistException, SQLException, UserException {
		PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + 15, 1));
		PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + 15, 1));
	}

	@Test (expected = PlaylistException.class)
	public void testDeletePlaylist() throws PlaylistException, SQLException, UserException {
		Playlist p=PlaylistDao.getInstance().getPlaylist(1, "list");
		PlaylistDao.getInstance().deletePlaylist(p.getPlaylistId());
		p=PlaylistDao.getInstance().getPlaylist(1, "list");
	}

	@Test
	public void testUpdateVideos() throws PlaylistException, SQLException, UserException {
		Playlist p = new Playlist("myList", 1);
		PlaylistDao.getInstance().createPlaylist(p);
		p.setName("newList");
		PlaylistDao.getInstance().updatePlaylist(p);
		Playlist get = PlaylistDao.getInstance().getPlaylist(p.getUserId(), "newList");
		Assert.assertTrue(p.getPlaylistName().equals(get.getPlaylistName()));
		Assert.assertTrue(p.getPlaylistId() == (get.getPlaylistId()));
		Assert.assertTrue(p.getUserId() == get.getUserId());
	}

	@Before
	public void init() throws PlaylistException, SQLException, UserException {
		String sql1 = "delete from playlists_has_videos";
		String sql2 = "delete from playlists";
		String sql3 = "alter table playlists AUTO_INCREMENT = 1";
		try (PreparedStatement ps = con.prepareStatement(sql1)) {
			ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql2)) {
			ps.executeUpdate();
		}
		try (PreparedStatement ps = con.prepareStatement(sql3)) {
			ps.executeUpdate();
		}
		//for test delete
		PlaylistDao.getInstance().createPlaylist(new Playlist("list", 1));
	}

}
