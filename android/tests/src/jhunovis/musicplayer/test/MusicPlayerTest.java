package jhunovis.musicplayer.test;

import jhunovis.musicplayer.MusicPlayer;
import android.test.AndroidTestCase;
import android.util.Log;

import android.net.Uri;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.util.*;
import java.io.IOException;


public class MusicPlayerTest extends AndroidTestCase {

		private MusicPlayer mMusicPlayer;
		
		/* Represents a connection to our local, private Music Player service.
		 * 
		 * This is basically taken from the API docs of android.app.Service.
		 */
		private ServiceConnection mConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMusicPlayer = ((MusicPlayer.LocalBinder)service).musicPlayer();				
			}

			public void onServiceDisconnected(ComponentName name) {
				mMusicPlayer = null;				
			}
		};
		
		public MusicPlayerTest() {
			super();
		}
		
		@Override
		public void setUp() throws Exception {
			super.setUp();
			Intent service = new Intent(getContext(), MusicPlayer.class);
			getContext().startService(service);
			getContext().bindService(new Intent(getContext(), 
		            MusicPlayer.class), mConnection, Context.BIND_AUTO_CREATE);			
		}

		@Override
		public void tearDown() throws Exception {
			super.tearDown();
			getContext().unbindService(mConnection);
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
			}catch(IOException x) {
				fail("IOException thrown by player.");
			}
			assertEquals(false, mMusicPlayer.isPlaying());
		}
		
		public void testPlayback() {
			String firstTrack = resIdToUri(R.raw.sterzer_for_you);
			List<String> playlist = Arrays.asList(
				firstTrack,
				resIdToUri(R.raw.tymphony_thrilled));
			mMusicPlayer.setPlaylist(playlist);
			try {
				mMusicPlayer.play();
			}catch(IOException x) {
				Log.d("MyLog", Log.getStackTraceString(x));
				fail("IOException thrown by player.");
			}
			assertEquals(true, mMusicPlayer.isPlaying());
			// this might accidentally fail under race-conditions:
			assertEquals(firstTrack, mMusicPlayer.currentTrack());
		}
		
		private String resIdToUri(int resid) {
			// Access resources of the unit test package instead of the main application.
			PackageManager pm = getContext().getPackageManager();
			Resources resources = null;
			try {
				resources = pm.getResourcesForApplication(this.getClass().getPackage().getName());
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
