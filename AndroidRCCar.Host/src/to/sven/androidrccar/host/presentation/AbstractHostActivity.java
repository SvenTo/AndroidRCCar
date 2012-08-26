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
package to.sven.androidrccar.host.presentation;

import to.sven.androidrccar.host.R;
import to.sven.androidrccar.host.controller.HostService;
import to.sven.androidrccar.host.controller.IHostService;
import to.sven.androidrccar.host.controller.IHostServiceListener;
import to.sven.androidrccar.host.controller.ServiceState;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Base Activity for the hosts activities. Does the binding to the {@link HostService} and 
 * provides some other generic functionality.
 * @author sven
 *
 */
public abstract class AbstractHostActivity extends Activity implements IHostServiceListener {

	/**
	 * Used as Log Tag.
	 * @see Log
	 */
	protected final String LOG_TAG = getClass().getSimpleName();
	
	/**
	 * Id of the options menu
	 * @see #onCreateOptionsMenu
	 */
	protected int menu_id = R.menu.menu;
	
    /**
     * access to the {@link HostService}
     */
    protected IHostService boundService;
    
    /**
     * Is the service bound?
     */
	private boolean isBound;
	
	/**
	 * The progress dialog will be shown {@link #onResume()}
	 * until the service is bound. 
	 */
	private ProgressDialog dialog;
	
	/**
	 * This result is used, when the application should exit.
	 */
	protected static final int RESULT_USER_EXIT = 42;
	
	/**
	 * Do no binding on Resume
	 * Used if the application should exit.
	 */
	private boolean noBinding = false;
	
	/**
	 * {@inheritDoc}
	 * Bind the service.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(LOG_TAG, "onResume");
		
		if(!noBinding) {
			if(dialog == null) {
				dialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.wait), true);
			}
			doBindService();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Unbind the service.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.i(LOG_TAG, "onPause");
		doUnbindService();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(menu_id, menu);
	    return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_about: showAbout(); 
	                            break;
	        case R.id.menu_settings: showSettings(); 
	                            break;
	    }
	    return true;
	}

	/**
	 * Shows the about dialog.
	 */
	private void showAbout() {
		 final Dialog aboutDialog = new Dialog(this, android.R.style.Theme_Dialog);
		 aboutDialog.setContentView(R.layout.info);
		 aboutDialog.setTitle(R.string.action_about);
         TextView textView = (TextView) aboutDialog.findViewById(R.id.infoTextVersion); 
         textView.setText(R.string.app_version);
         textView = (TextView) aboutDialog.findViewById(R.id.infoAuthor); 
 		 Linkify.addLinks(textView, Linkify.ALL);
 		 
         Button okBtn = (Button)aboutDialog.findViewById(R.id.info_ok_btn);
         okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				aboutDialog.dismiss();
			}
		 });
         
         aboutDialog.show();
	}
	
	/**
	 * Shows the settings.
	 */
	private void showSettings() {
        Intent settingsActivity = new Intent(getBaseContext(), HostPreferencesActivity.class);
        startActivity(settingsActivity);
	}
	
	/**
	 * Shows a dialog with the error from the {@link IHostService}.
	 */
	protected void showErrorDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(boundService.getErrorMsg())
			   .setTitle(R.string.error_dialog_title)
		       .setCancelable(false)
		       .setPositiveButton(R.string.quit, new OnClickListener() {
				@Override
				public void onClick(DialogInterface d, int which) {
					completeExit();
				}
			}); 
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void stateChanged(ServiceState state);
	
	/**
	 * Called when the connection to the service is established.
	 */
	protected abstract void onServiceConnected();

	/**
	 * Will called when  (dis)connected to the service. 
	 * @see ServiceConnection
	 */
    private ServiceConnection mConnection = new ServiceConnection() {
    	/**
    	 * {@inheritDoc}
    	 */
        @Override
		public void onServiceConnected(ComponentName className, IBinder service) {
        	boundService = ((HostService.HostBinder)service).getService();
        	boundService.setListener(AbstractHostActivity.this);
            AbstractHostActivity.this.onServiceConnected();
            dialog.cancel();
            dialog = null;
            
            Log.i(LOG_TAG, "onServiceConnected");
        }

        /**
         * {@inheritDoc}
         */
        @Override
		public void onServiceDisconnected(ComponentName className) {
        	Log.i(LOG_TAG, "onServiceDisconnected");
        	removeServiceBinding();
        }
    };
    
    /**
     * Establish a connection with the service.
     */
    void doBindService() {
    	Log.i(LOG_TAG, "doBindService");
    	if(!isBound) {
    		Log.i(LOG_TAG, "doBindService -> !isBound");
	    	startService(new Intent(this, HostService.class)); // TODO: This here?
	    	bindService(new Intent(this, HostService.class), mConnection, Context.BIND_ABOVE_CLIENT);
	    	isBound = true;
	    	Log.i(LOG_TAG, "doBindService -> done");
    	}
    }

    /**
     * Detach connection, if existing.
     */
    void doUnbindService() {
    	Log.i(LOG_TAG, "doUnbindService");
        if (isBound) {
        	Log.i(LOG_TAG, "doUnbindService -> isBound");
            unbindService(mConnection);
            removeServiceBinding();
        }
    }
    
    /**
     * Unsets the {@link HostService}-Listener and
     * sets binding to null and false.
     */
    private void removeServiceBinding() {
    	if(boundService != null) {
        	boundService.setListener(null);
        	boundService = null;    		
    	}
    	isBound = false;
    }
    
    /**
     * Exits the complete activity stack.
     */
    protected void completeExit() {
    	Log.i(LOG_TAG, "completeExit");
    	doUnbindService();
    	stopService(new Intent(this, HostService.class));
    	setResult(RESULT_USER_EXIT);
    	finish();
    }
    
    /**
     * The started activity is finished.
     * If the user will close the application,
     * the {@code resultCode} #RESULT_USER_EXIT is set and
     * this this activity will finish with the same result.
     * {@link #noBinding} is set to true.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Log.i(LOG_TAG, "onActivityResult: " + resultCode);
    	if(resultCode == RESULT_USER_EXIT) {
    		Log.i(LOG_TAG, "onActivityResult -> RESULT_USER_EXIT");
    		noBinding = true;
    		setResult(RESULT_USER_EXIT);
    		finish();
    	}
    }
}
