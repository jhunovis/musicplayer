package jhunovis.musicplayer;

import jhunovis.musicplayer.R;
import android.os.Bundle;
import android.app.Activity;
import android.widget.*;

public class MusicPlayerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
//    public void onResume(){
//    	super.onResume();
//        ImageView cover = (ImageView)findViewById(R.id.cover);
//        TextView title = (TextView)findViewById(R.id.songtitle);
//        int height = cover.getHeight();
//        int width = cover.getWidth();
//        title.setText(String.valueOf(height) + " / " + String.valueOf(width));            	
//    }
}