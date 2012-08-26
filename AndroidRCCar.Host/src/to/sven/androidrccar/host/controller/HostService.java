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
package to.sven.androidrccar.host.controller;

import java.net.Socket;

import to.sven.androidrccar.common.exception.ConnectionProblemException;
import to.sven.androidrccar.common.logic.contract.ILogic;
import to.sven.androidrccar.common.model.ConnectionParameter;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryConnector;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryConnectorListener;
import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryConnector;
import to.sven.androidrccar.host.communication.contract.ISocketConnector;
import to.sven.androidrccar.host.communication.contract.ISocketConnectorListener;
import to.sven.androidrccar.host.communication.impl.SocketConnector;
import to.sven.androidrccar.host.framework.HostDependencyContainer;
import to.sven.androidrccar.host.framework.IHostDependencyContainer;
import to.sven.androidrccar.host.logic.contract.IHostLogicListener;
import to.sven.androidrccar.host.logic.impl.HostLogic;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

// TODO: Implement http://developer.android.com/reference/android/os/PowerManager.html ?

/**
 * Controller of the Host application.
 * @author sven
 *
 */
public class HostService extends Service implements IHostService {
	
	/**
	 * Used as Log Tag.
	 * @see Log
	 */
	private static final String LOG_TAG = "HostService";
	
	/**
	 * A  {@link Intent} Action that indication that the USB accessory is attached.   
	 */
	public static final String ACTION_USB_ACCESSORY_ATTACHED = "to.sven.androidrccar.host.action.USB_ACCESSORY_ATTACHED_ACTION";
	
	/**
	 * The listener, that will be informed when the state changes.
	 */
	private IHostServiceListener serviceListener;

	/**
	 * Current state of the service.
	 */
	private ServiceState state = ServiceState.NOT_CONNECTED;
	
	/**
	 * The {@link HostServiceNotification}.
	 */
	private HostServiceNotification notification;
	
	/**
	 * The {@link IHostDependencyContainer}.
	 */
	private IHostDependencyContainer dependencyContainer;
	
	/**
	 * The {@link IAccessoryConnector}.
	 */
	private IAccessoryConnector accessoryConnector;
	
	/**
	 * The {@link ISocketConnector}.
	 */
	private ISocketConnector socketConnector;
	
	/**
	 * The {@link ILogic}.
	 */
	private ILogic logic;
	
	/**
	 * If a error occurred, this {@link String} contains the error message.
	 */
	private String errorMsg;
	
	/**
	 * This is the object that receives interactions from clients.
	 */
    private final IBinder binder = new HostBinder();
	
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class HostBinder extends Binder {
    	/**
    	 * Returns Service as {@link IHostService}
    	 * @return Service as {@link IHostService}
    	 */
    	public IHostService getService() {
            return HostService.this;
        }
    }
	
    /**
     * {@inheritDoc}
     */
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(LOG_TAG, "bind");
		return binder;
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void onCreate() {
    	Log.i(LOG_TAG, "onCreate");
    	
    	notification = new HostServiceNotification(this);
    	
    	dependencyContainer = new HostDependencyContainer(hostLogicListener, this, connectorListener);
    	dependencyContainer.setSocketConnectorListener(socketConnectorListener);
    	accessoryConnector = new AccessoryConnector(dependencyContainer);
    }
	
    /**
     * Implementation of the Listener Interface {@link IHostLogicListener}.
     */
    private final IHostLogicListener hostLogicListener = new IHostLogicListener() {
		
    	/**
    	 * If the accessory connector is not the reason for the disconnect,
    	 * run {@link HostService#executeSocketConnector()}.
    	 */
		@Override
		public void connectionLost(ConnectionProblemException ex) {
			Log.w(LOG_TAG, ex);
			// First check if the accessory connector is not the reason for the disconnect:
			if(accessoryConnector.accessoryConnected()) {
				logic = null;
				executeSocketConnector();
				changeState(ServiceState.CONNECTION_LOST);
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void connectedEstablished() {
			changeState(ServiceState.CONNECTED);
		}
	};
    
	/**
	 * Implementation of the Listener Interface {@link IAccessoryConnectorListener}.
	 */
    private final IAccessoryConnectorListener connectorListener = new IAccessoryConnectorListener() {
		
		@Override
		public void accessoryOpend() {
			executeSocketConnector();
			changeState(ServiceState.WAIT_FOR_OTHER_PHONE);
		}
		
		@Override
		public void accessoryDetached() {
			changeState(ServiceState.NOT_CONNECTED);
			// We don't destroy the service here, but we want to close all possible.
			onDestroy();
		}

		@Override
		public void error(int resId, Object... args) {
			errorMsg = getString(resId, args);
			changeState(ServiceState.ERROR);
		}
	};
	
	/**
	 * If the {@link #socketConnector} is not running, start a new 
	 * {@link SocketConnector}, that listens for a connecting client.
	 */
	private void executeSocketConnector() {
		if(socketConnector == null || socketConnector.finishedListening()) {
			socketConnector = new SocketConnector(dependencyContainer);
			socketConnector.listen();
		}
	}
	
	/**
	 * Implementation of the of the Listener Interface {@link ISocketConnectorListener}
	 */
	private final ISocketConnectorListener socketConnectorListener = new ISocketConnectorListener() {
		
		@Override
		public void error(int resId, Object... args) {
			errorMsg = getString(resId, args);
			changeState(ServiceState.ERROR);
		}
		
		@Override
		public void connectionEstablished(Socket socket) {
			dependencyContainer.setSocket(socket);
			logic = new HostLogic(dependencyContainer);
		}
	};

	/**
	 * If a {@link #ACTION_USB_ACCESSORY_ATTACHED} is passed a action, it tries to open 
	 * the USB accessory.
	 * @see Service#onStartCommand(Intent, int, int)
	 * @return returns sticky, to continue running. 
	 */
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        
        if(intent != null && ACTION_USB_ACCESSORY_ATTACHED.equals(intent.getAction())) {
        	accessoryConnector.tryOpenAccessory();
        }
        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

	/**
	 * Closes all 
	 */
    @Override
    public void onDestroy() {
        if(logic != null) {
        	logic.close();
        }
        if(socketConnector != null) {
        	socketConnector.cancel();
        }
        accessoryConnector.close();
        notification.cancel();
        
        Log.i(LOG_TAG, "OnDestroy");
    }
    
    /**
     * Set a new state of the service
     * @param newState The new {@link ServiceState}
     */
    public void changeState(ServiceState newState) {
    	this.state = newState;
    	notification.stateChanged(newState);
    	if(serviceListener != null) {
    		serviceListener.stateChanged(newState);
    	}
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setListener(IHostServiceListener listener) {
		this.serviceListener = listener;
	}

    /**
     * {@inheritDoc}
     */
	@Override
	public ConnectionParameter getConnectionDetails() {
		return socketConnector.getConnectionDetails();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getErrorMsg() {
		return errorMsg;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ServiceState getState() {
		return state;
	}
	
	/**
	 * If the service is in state {@link ServiceState#NOT_CONNECTED}
	 * the service should stop if the no client connected (when the activity is not visible).
	 * But rather checking only the state, we check if a accessory is connected, too.
	 * This is necessary, because the accessory permission dialog
	 * lead the activity to pause (so to unbind from the service).
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(LOG_TAG, "onUnbind");
		 
		if(state == ServiceState.NOT_CONNECTED && !accessoryConnector.accessoryConnected()) {
			Log.i(LOG_TAG, "onUnbind -> NOT_CONNECTED");
			stopSelf();
		}
		return true;
	}
}
