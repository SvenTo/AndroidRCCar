/*******************************************************************************
 * Copyright (C) 2012 Sven Nobis
 * 
 * This file is part of AndroidRCCar (http://androidrccar.sven.to)
 * 
 * AndroidRCCar is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************/
package to.sven.androidrccar.rtspcameratest;

import android.os.AsyncTask;
import android.util.Log;

import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

import de.kp.net.rtp.viewer.RtpVideoRenderer;

/**
 * Provides playing of a video stream.
 * @author sven
 *
 */
public class VideoClientService {

	/**
	 * Tag for {@link Log}
	 */
	private static final String LOG_TAG = "VideoClientService";
	
	/**
	 * The view where the video should be shown
	 */
	private final VideoSurfaceView videoView;
	
	/**
	 * Video-Renderer
	 */
	private RtpVideoRenderer renderer;
	
	/**
	 * Default constructor
	 * @param videoView The view where the video should be shown
	 */
	public VideoClientService(VideoSurfaceView videoView) {
		this.videoView = videoView;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void playStream(String url) {
		if(renderer != null) {
			stop();
		}
		try {
			new AsyncTask<String, Void, RtpVideoRenderer>() {

				@Override
				protected RtpVideoRenderer doInBackground(String... params) {
					try {
						return new RtpVideoRenderer(params[0]);
					} catch (Exception e) {
						// TODO:
						e.printStackTrace();
						return null;
					}
				}
				
				@Override
				protected void onPostExecute(RtpVideoRenderer result) {
					if(result != null) {
						renderer = result;
						renderer.setVideoSurface(videoView);
						renderer.open();
						renderer.start();
					}
				}
			}.execute(url);
		} catch (Exception e) {
			Log.e(LOG_TAG, "playStream", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void stop() {
		if(renderer != null) {
			renderer.stop();
			renderer.close();	
		}
	}

}
