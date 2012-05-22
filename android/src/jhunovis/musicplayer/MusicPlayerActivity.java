package jhunovis.musicplayer;

import jhunovis.musicplayer.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.GestureDetector;
import android.util.Log;
import android.util.DisplayMetrics;

//import android.widget.*;

public class MusicPlayerActivity extends Activity {

	public static final String TAG = "MusicPlayer";
	private GestureDetector gestureDetector;
	private float verticalFlipThreshold;
	private static final int[] LAYOUTS = {R.layout.bitmapgravitytest,
		R.layout.toosmall,
		R.layout.excactfit,
		R.layout.toolarge,
	};
	private int currentLayout = 0;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null){
			currentLayout = savedInstanceState.getInt("CURRENT_LAYOUT");
		}
		setContentView(LAYOUTS[currentLayout]);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		verticalFlipThreshold = metrics.widthPixels / 3f;
		Log.d(TAG, "Width is " + metrics.widthPixels);
		Log.d(TAG, "threshold is " + verticalFlipThreshold);
		gestureDetector = new GestureDetector(this, new MyGestureListener());

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float deltaX = e1.getX() - e2.getX();
			float deltaY = e1.getY() - e2.getY();
			if (e1.getPointerCount() == 1
					&& Math.abs(deltaX) > Math.abs(deltaY)) {
				if (Math.abs(deltaX) >= verticalFlipThreshold) {
					if (deltaX < 0) {
						// left to right
						Log.d(TAG, "left to right fling detected");
						nextLayout();
					} else {
						// right to left
						Log.d(TAG, "right to left fling detected");
						previousLayout();
					}
				}
			}
			return true;
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putInt("CURRENT_LAYOUT", currentLayout);
	}
	
	private void nextLayout(){
		currentLayout = (currentLayout + 1) % LAYOUTS.length;
		setContentView(LAYOUTS[currentLayout]);
	}
	
	private void previousLayout(){
		currentLayout = currentLayout==0 ? LAYOUTS.length-1 : currentLayout-1;
		setContentView(LAYOUTS[currentLayout]);		
	}
	
	// public void onResume(){
	// super.onResume();
	// ImageView cover = (ImageView)findViewById(R.id.cover);
	// TextView title = (TextView)findViewById(R.id.songtitle);
	// int height = cover.getHeight();
	// int width = cover.getWidth();
	// title.setText(String.valueOf(height) + " / " + String.valueOf(width));
	// }
}