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

/**
 * This activity will shown, when the Host is in {@link ServiceState#NOT_CONNECTED}.
 * @author sven
 *
 */
public class UnconnectedActivity extends AbstractHostActivity {
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unconnected);
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
	 * or starts {@link WaitForOtherPhoneActivity} if the State is not {@link ServiceState#NOT_CONNECTED}. 
	 * @param state The state of the {@link HostService}
	 */
	protected void handleState(ServiceState state) {
		switch (state) {
			case CONNECTED:
			case WAIT_FOR_OTHER_PHONE:
			case CONNECTION_LOST:
				startActivityForResult(new Intent(this, WaitForOtherPhoneActivity.class), 0);
				break;
			case ERROR:
				showErrorDialog();
				break;
		}
	}
}
