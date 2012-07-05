package jhunovis.musicplayer.test;

import jhunovis.musicplayer.MusicPlayer;
import android.test.ServiceTestCase;
import android.util.Log;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.*;
import java.io.IOException;

public class MusicPlayerTest extends ServiceTestCase<MusicPlayer> {

	public MusicPlayerTest() {
		super(MusicPlayer.class);
	}

	private MusicPlayer mMusicPlayer;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		startMusicPlayer();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		// No shutdown required! super-class already does it.
		// Double shutdown will result in a null-pointer-exception.
		// shutdownMusicPlayer();
	}

	private void startMusicPlayer() {
		Intent service = new Intent(getContext(), MusicPlayer.class);
		startService(service);
		mMusicPlayer = ((MusicPlayer.LocalBinder) bindService(service))
				.getMusicPlayer();
	}

	private void shutdownMusicPlayer() {
		// XXX: this will cause a null pointer exception
		shutdownService();
	}

	public void testSetPlaylist() {
		List<String> playlist = new ArrayList<String>();
		mMusicPlayer.setPlaylist(playlist);
		assertEquals(playlist, mMusicPlayer.getPlaylist());
	}

	public void testPlaybackWithEmptyPlaylist() {
		mMusicPlayer.setPlaylist(null);
		try {
			mMusicPlayer.play();
		} catch (IOException x) {
			fail("IOException thrown by player.");
		}
		assertEquals(false, mMusicPlayer.isPlaying());
	}

	public void testPlayback() {
		String firstTrack = resIdToUri(R.raw.sterzer_for_you);
		List<String> playlist = Arrays.asList(firstTrack,
				resIdToUri(R.raw.tymphony_thrilled));
		mMusicPlayer.setPlaylist(playlist);
		try {
			mMusicPlayer.play();
		} catch (IOException x) {
			Log.d("MyLog", Log.getStackTraceString(x));
			fail("IOException thrown by player.");
		}
		assertEquals(true, mMusicPlayer.isPlaying());
		// this might accidentally fail under race-conditions:
		assertEquals(firstTrack, mMusicPlayer.currentTrack());
	}

	private String resIdToUri(int resid) {
		// Access resources of the unit test package instead of the main
		// application.
		PackageManager pm = getContext().getPackageManager();
		Resources resources = null;
		try {
			resources = pm.getResourcesForApplication(this.getClass()
					.getPackage().getName());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Create a valid Android resource URI for the gived resource id.
		return ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
				+ resources.getResourcePackageName(resid) + '/'
				+ resources.getResourceTypeName(resid) + '/'
				+ resources.getResourceEntryName(resid);
	}
}
