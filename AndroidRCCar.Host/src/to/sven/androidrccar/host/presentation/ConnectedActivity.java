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
import android.os.Bundle;
import android.view.View;

/**
 * This activity will shown, when the Host is connected to the accessory and to the other phone  ({@link ServiceState#CONNECTED}).
 * @author sven
 *
 */
public class ConnectedActivity extends AbstractHostActivity {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.connected);
	}

	/**
	 * Exit the application.
	 * @param v Not used.
	 */
    public void quitClicked(@SuppressWarnings("unused") View v) {				
    	completeExit();
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
	 * Shows the error dialog ({@link #showErrorDialog()}) on State {@link ServiceState#ERROR}
	 * or finish the activity if the state is not {@link ServiceState#CONNECTED}. 
	 * @param state The state of the {@link HostService}
	 */
	protected void handleState(ServiceState state) {
		switch (state) {
			case WAIT_FOR_OTHER_PHONE:
			case NOT_CONNECTED:
			case CONNECTION_LOST:
				finish();
				break;
			case ERROR:
				showErrorDialog();
				break;
		}
	}
}
