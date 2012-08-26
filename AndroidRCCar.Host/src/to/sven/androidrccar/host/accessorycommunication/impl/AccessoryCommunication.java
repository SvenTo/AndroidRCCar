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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import to.sven.androidrccar.host.accessorycommunication.command.AbstractCommand;
import to.sven.androidrccar.host.accessorycommunication.command.AdjustSpeedCommand;
import to.sven.androidrccar.host.accessorycommunication.command.GetBatteryStateCommand;
import to.sven.androidrccar.host.accessorycommunication.command.GetProtocolVersionCommand;
import to.sven.androidrccar.host.accessorycommunication.command.ICommandListener;
import to.sven.androidrccar.host.accessorycommunication.command.RotateCameraCommand;
import to.sven.androidrccar.host.accessorycommunication.command.TurnCarCommand;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunicationListener;
import to.sven.androidrccar.host.accessorycommunication.exception.AccessoryConnectionProblemException;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import android.os.Handler;
import android.os.Looper;


/**
 * This class communicates with the µController.
 * 
 * It is realized with a message queue:
 * It uses a {@link Handler} that post the message (a {@link AbstractCommand}) to a 
 * communication thread. The thread sends the message and
 * wait until it gets the answer.
 * @author sven
 *
 */
public class AccessoryCommunication implements IAccessoryCommunication, ICommandListener {
	// TODO: Implement an send timeout? 
	// TODO: Call close on exit
	// TODO: Handle: onError with new Logic attached! -> IsAlive?
	
	/**
	 * Version of the implemented protocol between µController and Host.
	 */
	public final static short PROTOCOL_VERSION = 1;
	
	/**
	 * Number of bytes of a command.
	 */
	public final static int COMMAND_LENGTH = 16;
	
	/**
	 * Number of bytes of a response message.
	 */
	public final static int REPSONE_MESSAGE_LENGTH = 16;
	
	/**
	 * The {@link OutputStream} to send message to the µController.
	 */
	private final OutputStream outputStream;
	
	/**
	 * The {@link InputStream} to received messages from the µController.  
	 */
	private final InputStream inputStream;
	
	/**
	 * A monitor object, that is used to ensure that the {@link #communicationThreadHandler}
	 * is set after start of {@link #communicationThread} before {@link #startCommunication()} returns.
	 */
	private final Object monitor = new Object();
	
	/**
	 * A {@link Handler} to post message to the {@link #communicationThread}.
	 */
	private volatile Handler communicationThreadHandler;

	/**
	 * @see CarFeatures 
	 */
	private volatile CarFeatures carFeatures;
	
	/**
	 * A {@link Handler} associated with the applications main thread (the one with the activities).
	 */
	private final Handler mainThreadHandler; 
	
	/**
	 * The Listener that want to now, when something happens here.
	 */
	private IAccessoryCommunicationListener listener;
	
	/**
	 * Default Constructor
	 * @param fileDescriptor A {@link FileDescriptor} that contains the {@link InputStream} and {@link OutputStream}
	 * 						 to communicate with the µController.
	 * @param mainThreadHandler A {@link Handler} associated with the applications main thread (the one with the activities).
	 */
	public AccessoryCommunication(FileDescriptor fileDescriptor, Handler mainThreadHandler) {
		this(new FileOutputStream(fileDescriptor),
			 new FileInputStream(fileDescriptor),
			 mainThreadHandler);
	}	
	
	/**
	 * Default Constructor
	 * @param outputStream The {@link OutputStream} to send message to the µController.
	 * @param inputStream The {@link InputStream} to received messages from the µController.  
	 * @param mainThreadHandler A {@link Handler} associated with the applications main thread (the one with the activities).
	 */
	public AccessoryCommunication(OutputStream outputStream, InputStream inputStream, Handler mainThreadHandler) {
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		this.mainThreadHandler = mainThreadHandler;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startCommunication() throws IllegalStateException {
		if(listener == null) {
			throw new IllegalStateException("You need to set a listener before start!");
		}
		communicationThread.start();
		synchronized(monitor) {
				if(communicationThreadHandler == null) {
					try {
						monitor.wait();
					} catch (InterruptedException e) { /* We wait for it. */ }
				}
		}
		communicationThreadHandler.post(new GetProtocolVersionCommand(this));
	}
	
	/**
	 * The communication thread. It is initialized as a {@link Looper}.
	 * Use {@link #communicationThreadHandler} to post message it.
	 */
	private Thread communicationThread = new Thread() {
		@Override
		public void run() {
			Looper.prepare();
			
			synchronized(monitor) {
				communicationThreadHandler = new Handler();
				monitor.notifyAll();
			}
			
			Looper.loop();
		}
	};
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CarFeatures getCarFeatures() {
		return carFeatures;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCarFeatures(CarFeatures features) {
		this.carFeatures = features;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void adjustSpeed(float speed) {
		if(communicationThreadHandler != null) {
			communicationThreadHandler.post(new AdjustSpeedCommand(this, speed));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void turnCar(float rotation) {
		if(communicationThreadHandler != null) {
			communicationThreadHandler.post(new TurnCarCommand(this, rotation));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotateCamera(float pan, float tilt) {
		if(communicationThreadHandler != null) {
			communicationThreadHandler.post(new RotateCameraCommand(this, pan, tilt));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestBatteryState() {
		if(communicationThreadHandler != null) {
			communicationThreadHandler.post(new GetBatteryStateCommand(this));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setListener(IAccessoryCommunicationListener listener) {
		if(listener == null) {
			throw new IllegalArgumentException("listener is null.");
		}
		this.listener = listener;
	}

	/**
	 * Stops the communication thread.
	 */
	@Override
	public void close() throws IOException {
		if(communicationThreadHandler != null) {
			communicationThreadHandler.postAtFrontOfQueue(new Runnable() {
				@Override
				public void run() {
					Looper.myLooper().quit(); 
				}
			});
			communicationThreadHandler = null;
		}
		// TODO: Close Streams?
	}

	/**
	 * {@inheritDoc}
	 * (Post message into the main thread.)
	 */
	@Override
	public void protocolVersionNotMatch(final short hostVersion,
										final short microControllerVersion) {
		mainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO: Close Connection? Stop Thread?
				listener.protocolVersionNotMatch(hostVersion, microControllerVersion);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * (Post message into the main thread.)
	 */
	@Override
	public void connectionInitialized() {
		mainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				listener.connectionInitialized();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * (Post message into the main thread.)
	 */
	@Override
	public void batteryStateReceived(final float chargingLevel) {
		mainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				listener.batteryStateReceived(chargingLevel);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * (Post message into the main thread.)
	 */
	@Override
	public void batteryNearEmpty() {
		mainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO: Close Connection? Stop Thread?
				listener.batteryNearEmpty();
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * (Post message into the main thread.)
	 */
	@Override
	public void errorReceived(final String message) {
		mainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				listener.errorReceived(message);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connectionProblem(final AccessoryConnectionProblemException ex) {
		// TODO: communicationThreadHandler.getLooper().quit();
		mainThreadHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO: Call Close?
				listener.connectionProblem(ex);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postCommand(AbstractCommand command) {
		if(communicationThreadHandler == null) {
			throw new IllegalStateException("The communication wasn't started or was already stopped.");
		}
		communicationThreadHandler.post(command);
	}
}
