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
package to.sven.androidrccar.host.accessorycommunication.impl;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import to.sven.androidrccar.common.R;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunicationListener;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryConnector;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryConnectorListener;
import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.framework.IHostDependencyContainer;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

// TODO: When USB Accessory is attached!
// TODO: Check if it's the correct Accessory.

/**
 * Initializes the connection to the accessory:
 * - Requesting permission, if need
 * - Open accessory
 * - Passing the opened {@link FileDescriptor} to {@link IAccessoryCommunication}.
 * - Inform the {@link IAccessoryConnectorListener} when the {@link IAccessoryCommunication} is initialized.
 * The class informs the listener when the accessory is disconnects.
 * @author sven
 */
public class AccessoryConnector implements IAccessoryConnector {

	/**
	 * Used as Log Tag.
	 * @see Log
	 */
	private static final String LOG_TAG = "AccessoryConnector";
	
	/**
	 * {@link Context}
	 */
	private final Context context;
	
	/**
	 * The intent action identifier that is used for the permission request. 
	 */
	private static final String ACTION_USB_PERMISSION = "to.sven.androidrccar.host.accessorycommunication.USB_PERMISSION";
	
	/**
	 * @see UsbManager
	 */
	private UsbManager usbManager;
	
	/**
	 * The permission intent, if one is send.
	 */
	private PendingIntent permissionIntent = null;
	
	/**
	 * Contains the {@link UsbAccessory} if one was opened.
	 */
	private UsbAccessory accessory;
	
	/**
	 * Contains the {@link ParcelFileDescriptor} that contains 
	 * the {@link InputStream} and {@link OutputStream}
	 * to communicate with the µController.
	 */
	private ParcelFileDescriptor paracelFD;
	
	/**
	 * The {@link IHostDependencyContainer}
	 */
	private IHostDependencyContainer dc;

	/**
	 * If a {@link UsbAccessory} was opened, it contains the created {@link IAccessoryCommunication}.
	 */
	private IAccessoryCommunication accessoryCommunication;
	
	/**
	 * Creates a new {@link AccessoryConnector}.
	 * It registers the {@link #usbReceiver} and calls
	 * {@link #tryOpenAccessory()}.
	 * @param dependencyContainer The {@link IHostDependencyContainer}
	 */
	public AccessoryConnector(IHostDependencyContainer dependencyContainer) {
		this.context = dependencyContainer.getContext();
		this.dc = dependencyContainer;
		
		this.usbManager = UsbManager.getInstance(context);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		context.registerReceiver(usbReceiver, filter);
		
		tryOpenAccessory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tryOpenAccessory() {
		// If accessory is open, do nothing:
		if(paracelFD != null) return;
		
		UsbAccessory[] accessories = usbManager.getAccessoryList();
		UsbAccessory acc = (accessories == null ? null : accessories[0]);
		if (acc != null) {
			if (usbManager.hasPermission(acc)) {
				openAccessory(acc);
			} else {
				synchronized (usbReceiver) {
					if(permissionIntent == null) {
						permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
						usbManager.requestPermission(acc, permissionIntent);
					}
				}
			}
		}	
	}
	
	/**
	 * Open the accessory and passing it a created {@link IAccessoryCommunication}. 
	 * @param acc The {@link UsbAccessory} that should be opened.
	 */
	private void openAccessory(UsbAccessory acc) {
		paracelFD = usbManager.openAccessory(acc);
		if(paracelFD != null) {
			this.accessory = acc;
			
			accessoryCommunication = dc.getFactory()
				   			           .createAccessoryCommunication(paracelFD.getFileDescriptor(),
						   			      							 dc.getHandler());
			accessoryCommunication.setListener(accListener);
			accessoryCommunication.startCommunication();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		closeAccessory();
		if(permissionIntent != null) {
			permissionIntent.cancel();
			permissionIntent = null;
		}
		try {
			context.unregisterReceiver(usbReceiver);
		} catch(IllegalArgumentException ex) {
			// Is thrown when the receiver is not registered.
		}
	}
	
	/**
	 * Closes connection to the accessory (, if there is one):
	 * - {@link #accessoryCommunication}
	 * - {@link #paracelFD}
	 */
	private void closeAccessory() {
		if(accessoryCommunication != null) {
			try {
				accessoryCommunication.close();
			} catch (IOException e) {
				Log.w(LOG_TAG, e);
			}
			accessoryCommunication = null;
		}
		if(paracelFD != null) {
			try {
				paracelFD.close();
			} catch (IOException e) {
				Log.w(LOG_TAG, e);
			}
			paracelFD = null;
		}
		accessory = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accessoryConnected() {
		return (usbManager.getAccessoryList() != null);
	}
	
	/**
	 * Receives the answer to the requested permission
	 * or if the {@link UsbAccessory} was detached.
	 */
	private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctxt, Intent intent) {
			String action = intent.getAction();
			Log.d(LOG_TAG, "usbReceiver.onReceive -> action: " + action);
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory acc = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(acc);
					} else {
						// Do something here?
						Log.d(LOG_TAG, "Permission denied for accessory " + acc);
					}
					permissionIntent = null;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory closedAccessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(closedAccessory)) {
					closeAccessory();
					dc.getAccessoryConnectorListener().accessoryDetached();
				}
			}
		}
	};
	
	/**
	 * Something went wrong during connecting to the accessory:
	 * The listener will be informed, and #close() is called.
	 * @param resId Android String Resource id 
	 * @param args Additional arguments that will be used to format the string resource.
	 * @see Context#getText(int)
	 * @see String#format(String, Object...)
	 */
	private void handleError(int resId, Object... args) {
		dc.getAccessoryConnectorListener()
		  .error(resId, args);
		close();
	}
	
	/**
	 * Handles events from the {@link #accessoryCommunication}.
	 */
	private final IAccessoryCommunicationListener accListener = new IAccessoryCommunicationListener() {
	
		@Override
		public void protocolVersionNotMatch(short hostVersion,
				short microControllerVersion) {
			handleError(R.string.error_protocol_version_not_match, hostVersion, microControllerVersion);
		}
		
		@Override
		public void errorReceived(String message) {
			handleError(R.string.error_accessory_send_error, message);
		}
		
		@Override
		public void connectionProblem(AccessoryConnectionProblemException ex) {
			handleError(R.string.error_accessory_connection_problem, ex);
		}
		
		/**
		 * Informs the {@link IAccessoryConnectorListener} that the is opened. 
		 */
		@Override
		public void connectionInitialized() {
			dc.setAccessoryCommunication(accessoryCommunication);
			dc.getAccessoryConnectorListener()
			  .accessoryOpend();
		}
		
		/**
		 * @throws UnsupportedOperationException Invalid here.
		 */
		@Override
		public void batteryStateReceived(float chargingLevel) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void batteryNearEmpty() {
			handleError(R.string.error_accessory_battery_empty);
		}
	}; 
}
