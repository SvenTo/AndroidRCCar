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
package to.sven.androidrccar.client.presentation;

import java.net.Socket;

import to.sven.androidrccar.client.R;
import to.sven.androidrccar.client.framework.ClientDependencyContainer;
import to.sven.androidrccar.client.logic.contract.IClientLogic;
import to.sven.androidrccar.client.logic.contract.IClientLogicListener;
import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.client.presentation.view.BatteryView;
import to.sven.androidrccar.client.presentation.view.BearingOfCarView;
import to.sven.androidrccar.client.presentation.view.DistanceView;
import to.sven.androidrccar.client.presentation.view.OnVirtualJoystickMoveListener;
import to.sven.androidrccar.client.presentation.view.VirtualJoystickView;
import to.sven.androidrccar.common.exception.ConnectionProblemException;
import to.sven.androidrccar.common.model.ConnectionParameter;
import to.sven.androidrccar.common.utils.AsyncTaskResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.orangelabs.rcs.platform.AndroidFactory;
import com.orangelabs.rcs.provider.settings.RcsSettings;
import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

/**
 * This activity allows the user to control the car.
 * @author sven
 *
 */
public class ClientActivity extends Activity {

	/**
	 * Tag for {@link Log}
	 */
	private static final String LOG_TAG = "ClientActivity";

	/**
	 * The {@link VirtualJoystickView}
	 */
	private VirtualJoystickView joystickView;
	/**
	 * The {@link BatteryView}
	 */
	private BatteryView batteryView;
	/**
	 * The {@link DistanceView}
	 */
	private DistanceView distanceView;
	/**
	 * The {@link BearingOfCarView}
	 */
	private BearingOfCarView bearingView;
	/**
	 * The speed text.
	 */
	private TextView speedView;
	
	/**
	 * This dialog will be shown until the client is connected.
	 */
	private ProgressDialog dialog;
	/**
	 * A {@link AlertDialog} for showing a error.
	 */
	private AlertDialog alert;

	/**
	 * The {@link IClientLogic}
	 */
	private IClientLogic logic;
	
	/**
	 * The {@link ConnectionParameter}
	 */
	private ConnectionParameter connectionParameter;

	/**
	 * The activity is on Stop.
	 */
	private boolean isOnStop;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.client);
		
		// Set application context ... skipping FileFactory
		AndroidFactory.setApplicationContext(getApplicationContext());
        // Instantiate the settings manager
        RcsSettings.createInstance(getApplicationContext());
        
		joystickView = (VirtualJoystickView)findViewById(R.id.joystickView);
		joystickView.setOnVirtualJoystickMoveListener(joystickMoveListener);
		batteryView = (BatteryView)findViewById(R.id.batteryView);
		distanceView = (DistanceView)findViewById(R.id.distanceView);
		bearingView = (BearingOfCarView)findViewById(R.id.bearingOfCarView);
		speedView = (TextView)findViewById(R.id.speedView);

		connectionParameter = new ConnectionParameter(getIntent().getData().toString());
	}
	
	/**
	 * Show connection dialog, disable sticks and connect.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		isOnStop = false;
		dialog = ProgressDialog.show(this, getString(R.string.connecting), getString(R.string.wait), true);
		
		// TODO: Use this?
//		batteryView.setVisibility(View.INVISIBLE);
//		speedView.setVisibility(View.INVISIBLE);
//		distanceView.setVisibility(View.INVISIBLE);
//		bearingView.setVisibility(View.INVISIBLE);
		joystickView.setEnabled(false);
		joystickView.enableLeftStick(false);
		joystickView.enableRightStick(false);
		
		// Connect to the Host:
		connectTask = new ConnectTask();
		connectTask.execute((Object)null);
	}
	
	/**
	 * Disconnect.
	 */
	@Override
	protected void onStop() {
		super.onStop();
		isOnStop = true;
		
		connectTask.cancel(true);
		if(logic != null) {
			// TODO: no error?
			logic.close();
			logic = null;
		}
		
		if(dialog != null) {
			dialog.cancel();
		}
	}

	/**
	 * The instance of the task that should connect the client with the host.
	 */
	private ConnectTask connectTask;
		

	/**
	 * The Task that should connect the client with the host.
	 */
	private class ConnectTask extends AsyncTask<Object, Object, AsyncTaskResult<Socket>> {

				/**
				 * Connecting to the Host.
				 */
				@Override
				protected AsyncTaskResult<Socket> doInBackground(
						Object... params) {
					try {
						Socket socket = new Socket(connectionParameter.host, connectionParameter.port);
						return new AsyncTaskResult<Socket>(socket);
					} catch (Exception e) {
						return new AsyncTaskResult<Socket>(e);
					}
				}
				
				/**
				 * Connection is open, initialize the logic.
				 */
				@Override
				protected void onPostExecute(AsyncTaskResult<Socket> result) {
					if(result.getError() == null) {
						ClientDependencyContainer dc = 
								new ClientDependencyContainer(logicListener,
															  result.getResult(),
															  ClientActivity.this,
															  connectionParameter,
															  getVideoView());
						logic = new ClientLogic(dc);
					} else {
						Log.e(LOG_TAG, "IOException", result.getError());
						showErrorDialog(R.string.error_open_connetion, result.getError());
					}
				}
	}
	
	/**
	 * Returns the video surface.
	 * @return {@link VideoSurfaceView}
	 */
	private VideoSurfaceView getVideoView() {
		return (VideoSurfaceView)findViewById(R.id.video_view);
	}
	
	/**
	 * Implementation of {@link IClientLogicListener}.
	 */
	private final IClientLogicListener logicListener = new IClientLogicListener() {
		
		@Override
		public void connectionLost(ConnectionProblemException ex) {
			Log.e(LOG_TAG, "ConnectionProblemException", ex);
			showErrorDialog(R.string.error_unspecified, ex);
		}
		
		@Override
		public void setSpeed(float speed) {
			speedView.setVisibility(View.VISIBLE);
			speedView.setText(String.format("%.2f ", speed) + getString(R.string.meterpersecound));
		}
		
		@Override
		public void setDistance(float distance, float bearingTo) {
			distanceView.setVisibility(View.VISIBLE);
			distanceView.setDistance(distance, bearingTo);
		}
		
		@Override
		public void setBearing(float bearing) {
			bearingView.setVisibility(View.VISIBLE);
			bearingView.setBearing(bearing);
		}
		
		@Override
		public void setBatteryPower(final float chargingLevel) {
			batteryView.setVisibility(View.VISIBLE);
			batteryView.setChargingLevel(chargingLevel);
		}
		
		@Override
		public void enableTilt(float min, float max) {
			joystickView.enableLeftStick(true);
		}
		
		@Override
		public void enablePan(float min, float max) {
			joystickView.enableLeftStick(true);
		}
		
		@Override
		public void enableDriving(boolean adjustableSpeed, boolean driveBackward) {
			joystickView.enableRightStick(true);
			
			if(dialog != null) {
				dialog.cancel();
				dialog = null;
			}
		}
		
		@Override
		public void authenticationFalied(String reason) {
			Log.e(LOG_TAG, "authenticationFalied: " + reason);
			showErrorDialog(R.string.error_authentication_falied, reason);
		}
	};
	
	/**
	 * Will be shown when an error occurs.
	 * @param resId The string id of the error message.
	 * @param args Additional information that would be formatted into the message.
	 */
	private void showErrorDialog(int resId, Object... args) {
		if(dialog != null) {
			dialog.cancel();
			dialog = null;	
		}
		
		if(alert == null && !isOnStop) {
			String msg = getString(resId, args);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(msg)
				   .setTitle(R.string.error_dialog_title)
			       .setCancelable(false)
			       .setPositiveButton(android.R.string.ok, new OnClickListener() {
						@Override
						public void onClick(DialogInterface d, int which) {
							finish();
						}
			       }); 
			alert = builder.create();
			alert.show();
		}
	}
	
	/**
	 * Implementation of {@link OnVirtualJoystickMoveListener}.
	 */
	private final OnVirtualJoystickMoveListener joystickMoveListener = new OnVirtualJoystickMoveListener() {

		/**
		 * Wait until this before sending the next
		 */
		private long nextSendl;
		
		/**
		 * Wait until this before sending the next
		 */
		private long nextSendr;

		@Override
		public void onRightMove(float x, float y) {
			long now = System.currentTimeMillis();
			// TODO: Not here & this better?
			if((now > nextSendr || y == 0)) {
				logic.adjustSpeed(-y);
				logic.turnCar(x);
				nextSendr = now+100;
			}
		}
		
		@Override
		public void onLeftMove(float x, float y) {
			long now = System.currentTimeMillis();
			// TODO: Not here & this better?
			if((now > nextSendl || y == 0 || x == 0)) {
				logic.rotateCamera(x*180, y*180);
				nextSendl = now+100;
			}
		}
	};
}
