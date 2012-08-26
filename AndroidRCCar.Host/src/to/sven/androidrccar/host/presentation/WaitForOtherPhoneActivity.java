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
import to.sven.androidrccar.host.controller.ServiceState;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * This activity will shown, when the Host is in {@link ServiceState#WAIT_FOR_OTHER_PHONE}.
 * @author sven
 *
 */
public class WaitForOtherPhoneActivity extends AbstractHostActivity {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.wait_for_other_phone);
	}

	/**
	 * Exit the application.
	 * @param v Not used.
	 */
    public void cancelClicked(@SuppressWarnings("unused") View v) {		
    	completeExit();
    }
    
    /**
     * Send the connection details, via mail, etc.
     * @param v Not used.
     */
    public void sendClicked(@SuppressWarnings("unused") View v) {
    	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    	sharingIntent.setType("text/plain");
    	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, boundService.getConnectionDetails().toConnectionURL());
    	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getText(R.string.send_connection_details_subject));
    	startActivity(Intent.createChooser(sharingIntent, getText(R.string.send_via)));
    }

    /**
     * Calls {@link #handleState(ServiceState)}
     */
	@Override
	public void stateChanged(ServiceState state) {
		handleState(state);
	}

    /**
     * Calls {@link #handleState(ServiceState)}
     */
	@Override
	protected void onServiceConnected() {
		handleState(boundService.getState());
	}
	
	/**
	 * - Shows the error dialog ({@link #showErrorDialog()}) on State {@link ServiceState#ERROR}.
	 * - Finishes activity on State {@link ServiceState#NOT_CONNECTED}.
	 * - Starts activity {@link ConnectedActivity} on State {@link ServiceState#CONNECTED}.
	 * else sets some text according to the state.
	 * @param state The state of the {@link HostService}
	 */
	protected void handleState(ServiceState state) {
		switch (state) {
			case CONNECTION_LOST:
				((TextView)findViewById(R.id.connection_description)).setText(R.string.connection_lost);
			case WAIT_FOR_OTHER_PHONE:
				((TextView)findViewById(R.id.connection_details)).setText(boundService.getConnectionDetails().toString());
				break;
			case CONNECTED:
				startActivityForResult(new Intent(this, ConnectedActivity.class), 0);
				break;
			case NOT_CONNECTED:
				finish();
				break;
			case ERROR:
				showErrorDialog();
				break;
		}
	}
}
