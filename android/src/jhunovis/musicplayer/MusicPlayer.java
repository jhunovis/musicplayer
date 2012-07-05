package jhunovis.musicplayer;

import android.media.*;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
//import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;
import android.widget.Toast;

import java.util.*;
import java.io.IOException;

/**
 * Allmost all of the service stub code is lifted from the API docs of class
 * Service.
 * 
 * @author bon
 * 
 */
public class MusicPlayer extends android.app.Service {

	// Service itself is a context
	// private Context mContext;
	private MediaPlayer mMediaPlayer;
	private List<String> mPlaylist;

	// public MusicPlayer(){
	// mContext = c;
	// }

	public void setPlaylist(List<String> playlist) {
		if (playlist == null)
			mPlaylist = new ArrayList<String>();
		else
			mPlaylist = playlist;
	}

	public List<String> getPlaylist() {
		return mPlaylist;
	}

	public void play() throws IOException {
		if (isPlaying())
			return;
		String currentTrack = currentTrack();
		if (currentTrack != null) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setDataSource(this, Uri.parse(currentTrack));
			mMediaPlayer.prepare();
			mMediaPlayer.start();
		}
	}

	public boolean isPlaying() {
		return false;
	}

	public String currentTrack() {
		if (mPlaylist.isEmpty())
			return null;
		else
			return mPlaylist.get(0);
	}

	/*
	 * Service Interface and operation. Based on the local service example 
	 * of class android.app.Service.
	 */

	private NotificationManager mNM;
	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = R.string.music_player_started;

	/*
	 * A instance of this class is returned when bind
	 */
	public class LocalBinder extends Binder {
		public MusicPlayer getMusicPlayer() {
			return MusicPlayer.this;
		}
	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("MusicPlayer", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		// Tell the user we stopped.
		Toast.makeText(this, R.string.music_player_stopped, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification.Builder(this)
				.setContentTitle(getText(R.string.music_player_started))
				.setSmallIcon(android.R.drawable.ic_media_play)
				.getNotification();

		// The PendingIntent to launch our activity if the user selects this
		// notification
		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// new Intent(this, LocalServiceActivities.Controller.class), 0);

		mNM.notify(NOTIFICATION, notification);
	}
}
