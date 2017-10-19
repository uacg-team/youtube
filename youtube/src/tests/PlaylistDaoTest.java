package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import model.Playlist;
import model.PlaylistDao;
import model.exceptions.playlists.PlaylistException;
import model.exceptions.user.UserException;

public class PlaylistDaoTest {
	@Test
	public void testCreatePlaylist() throws PlaylistException, SQLException, UserException {
		// creating 10 playlist for user_id=1;
		for (int i = 0; i < 10; i++) {
			PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + i, 1));
		}
		// creating 10 playlist for user_id=2;
		for (int i = 0; i < 10; i++) {
			PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + i, 2));
		}
	}

	@Test(expected = PlaylistException.class)
	public void testCreatePlaylistWithSameNames() throws PlaylistException, SQLException, UserException {
		// creating 10 playlist for user_id=1;
		PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + 15, 1));
		PlaylistDao.getInstance().createPlaylist(new Playlist("playlist" + 15, 1));
	}

	@Test
	public void testUpdatePlaylist() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeletePlaylist() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlaylistsLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlaylist() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadVideosInPlaylist() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddVideo() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveVideo() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPlaylists() {
		fail("Not yet implemented");
	}

	@Test
	public void testSearchPlaylist() {
		fail("Not yet implemented");
	}

}
