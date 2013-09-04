package to.sven.androidrccar.rtspcameratest;

import com.orangelabs.rcs.platform.AndroidFactory;
import com.orangelabs.rcs.provider.settings.RcsSettings;
import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	private VideoClientService videoService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Set application context ... skipping FileFactory
		AndroidFactory.setApplicationContext(getApplicationContext());
        // Instantiate the settings manager
        RcsSettings.createInstance(getApplicationContext());
		
		VideoSurfaceView v = (VideoSurfaceView) findViewById(R.id.video_view);
		videoService = new VideoClientService(v);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		String url = "rtsp://212.60.217.2:8080/kupandroid.3gp";
		videoService.playStream(url);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		videoService.stop();
	}

}
