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

import to.sven.androidrccar.host.controller.HostService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Helper Activity that delegates the USB_ACCESSORY_ATTACHED intent to the service.
 * This is necessary, because the intent can only received by an activity and not by an service/broadcast receiver:
 * See: http://stackoverflow.com/questions/11098332/usb-accessory-catch-usb-accessory-attached-by-intent-filter-of-service
 * @author sven
 *
 */
public class AccessoryAttachedActivity extends Activity {

	/**
	 * Used as Log Tag.
	 * @see Log
	 */
	private static final String LOG_TAG = "AccessoryAttachedActivity";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(LOG_TAG, "onCreate");
		
		Intent serviceIntent = new Intent(this, HostService.class);
		serviceIntent.setAction(HostService.ACTION_USB_ACCESSORY_ATTACHED);
		startService(serviceIntent);
		
		finish();
	}
}
