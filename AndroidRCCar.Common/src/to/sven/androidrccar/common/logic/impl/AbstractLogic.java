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
package to.sven.androidrccar.common.logic.impl;

import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

import to.sven.androidrccar.common.communication.contract.IRemoteCommunication;
import to.sven.androidrccar.common.communication.contract.IRemoteCommunicationListener;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.exception.ConnectionProblemException;
import to.sven.androidrccar.common.exception.UnsupportedMessageException;
import to.sven.androidrccar.common.framework.AbstractDependencyContainer;
import to.sven.androidrccar.common.framework.IDependencyContainer;
import to.sven.androidrccar.common.framework.IFactory;
import to.sven.androidrccar.common.logic.contract.ILogic;
import to.sven.androidrccar.common.logic.contract.ILogicListener;
import to.sven.androidrccar.common.logic.handler.ILogicHandlerFacade;
import to.sven.androidrccar.common.logic.handler.IMessageHandler;
import to.sven.androidrccar.common.service.contract.ILocationService;
import android.util.Log;

/**
 * General implementation for Host and Client.
 * It provides the relaying of incoming message to the concrete {@link IMessageHandler}
 * and error handling.
 * (On an error the connection will be closed.) 
 * 
 * @author sven
 * @param <TDependencyContainer> A concrete {@link IDependencyContainer} interface for dependency injection.
 * @param <TListener> The concrete type of a {@link ILogicListener}, that uses this Logic (Most likely an Activity).
 */
public abstract class AbstractLogic<TDependencyContainer extends IDependencyContainer<? extends IFactory, TListener>,
									TListener extends ILogicListener>
	implements IRemoteCommunicationListener, ILogicHandlerFacade<TDependencyContainer>, ILogic {
	
	/**
	 * Was closed called?
	 */
	private boolean closed = false; 
	
	/**
	 * Used as Log Tag.
	 * @see Log
	 */
	private static final String LOG_TAG = "AbstractLogic";
	
	/**
	 * Used for the Communication with the Client/Host.
	 * @see IRemoteCommunication
	 */
	private final IRemoteCommunication remoteCommunication;
	
	/**
	 * Contains the canonical names and the instances of all {@link IMessageHandler} that 
	 * should handle {@link Message}s in the current state of this class.
	 */
	private final SortedMap<String,IMessageHandler<?>> messageHandlers = 
			new TreeMap<String,IMessageHandler<?>>();
	
	/**
	 * The one that that want to know, when something happen here (Most likely an Activity)
	 */
	private final TListener listener;
	
	/**
	 * A concrete {@link AbstractDependencyContainer} for dependency injection
	 */
	private final TDependencyContainer container;
	
	/**
	 * Initializes the {@link AbstractLogic}
	 * @param container A {@link IDependencyContainer} for dependency injection
	 */
	public AbstractLogic(TDependencyContainer container) {
		this.container = container;
		this.listener = container.getLogicListener();
		this.remoteCommunication = container.getFactory()
										    .createRemoteCommuncation(container, this);
	}
	
	/**
	 * This method should called, when the concrete logic is initialized.
	 * Starts the receiving of messages.
	 */
	protected void initialized() {
		remoteCommunication.startMessageListener();
	}
	
	/**
	 * Returns the concrete {@link IDependencyContainer} interface.
	 * @return {@link #container}
	 */
	@Override
	public TDependencyContainer getDependency() {
		return container;
	}
	
	/**
	 * Called from the {@link #remoteCommunication} when a message is received.
	 * Searches for an matching {@link IMessageHandler} and calls it with the message.
	 * If no matching handler is found, {@link #handleUnsupportedMessage(Message)} is called.
	 */
	@Override
	public void messageReceived(Message message) {
		String messageType = message.getClass().getCanonicalName();
		IMessageHandler<?> messageHandler = messageHandlers.get(messageType);
		if(messageHandler != null) {
			handleMessage(message, messageHandler);
		} else {
			handleUnsupportedMessage(message);
		}
	}
	
	/**
	 * Call the {@code messageHandler} with the given {@code message}. 
	 * @param <T> The type of message
	 * @param message Message to handle
	 * @param messageHandler Handler, that should handle the message
	 */
	@SuppressWarnings("unchecked") // Only called with the correct handler 
	private <T extends Message> void handleMessage(Message message, IMessageHandler<T> messageHandler) {
		try {
			messageHandler.handleMessage((T)message);
		} catch(Exception e) {
			handleError(e); // TODO: Test thiz.
		}
	}
	
	/**
	 * Handles message that are not supported by the current state of the object.
	 * @param message {@link Message} that is not supported.
	 */
	protected void handleUnsupportedMessage(Message message) {
		handleError(new UnsupportedMessageException(message));
	}

	/**
	 * Register a new {@link IMessageHandler} that should handle messages.
	 * @param messageHandlerClass {@link Class} of {@link IMessageHandler} that should be added
	 * @param <T> Concrete Type of {@link IMessageHandler}
	 * 
	 */
	@Override
	public <T extends IMessageHandler<?>> void registerMessageHandler(Class<T> messageHandlerClass) {
		registerMessageHandler(getDependency().getFactory()
										 			 .createMessageHandler(messageHandlerClass, this));
	}
	
	/**
	 * Register a new {@link IMessageHandler} that should handle messages.
	 * @param messageHandler The {@link IMessageHandler} to register
	 */
	private void registerMessageHandler(IMessageHandler<? extends Message> messageHandler) {
		messageHandlers.put(messageHandler.getMessageType().getCanonicalName(), messageHandler);
	}
	
	/**
	 * Removes all registered {@link IMessageHandler}s.
	 */
	@Override
	public void clearMessageHandlers() {
		messageHandlers.clear();
	}

	/**
	 * Called from the {@link #remoteCommunication} when a problem with the exception occurs.
	 * @param ex The Problem
	 */
	@Override
	public void connectionProblem(Exception ex) {
		handleError(ex, "connectionProblem");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		try {
			closed = true;
			// The connections should closed, so no we expect no messages anymore:
			messageHandlers.clear();
			remoteCommunication.close(); // TODO: Implicit tested this?
			
			ILocationService locationService = getDependency().getLocationService();
			if(locationService != null) {
				locationService.stopListen();
			}
		} catch(IOException ex) {
			Log.w(LOG_TAG, "close", ex);
		}
	}
	
	/**
	 * Sends a message to the {@link #remoteCommunication}.
	 * @param message The message
	 */
	@Override
	public void sendMessage(Message message) {
		try {
			Log.d(LOG_TAG, message.getClass().getSimpleName()); // TODO: rm Debug
			remoteCommunication.sendMessage(message);
		}
		catch(IOException ex)
		{
			handleError(ex, "sendMessage");
		}
	}
	
	/**
	 * Will be called on an error.
	 * Closes the connection and logs the Exception {@code ex}.
	 * @param ex The error
	 */
	@Override
	public void handleError(Exception ex) {
		handleError(ex, null);
	}
	
	/**
	 * Will be called on an error.
	 * Closes the connection and logs the Exception {@code ex}.
	 * @param ex The error
	 * @param logMessage Additional message for logging
	 */
	@Override
	public void handleError(Exception ex, String logMessage) {
		Log.e(LOG_TAG, logMessage, ex);
		
		if(!closed) {
			close();
			ConnectionProblemException newEx = (logMessage == null)?
					new ConnectionProblemException(ex):
					new ConnectionProblemException(logMessage, ex);
			listener.connectionLost(newEx);
		}
	}
	
	// TODO: Test thiz:
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableLocationService() {
		ILocationService service = getDependency().getLocationService();
		if(service == null) {
			service = getDependency().getFactory()
									 .createLocationService(getDependency().getContext());
			
			getDependency().setLocationService(service);
			service.startListen();
		}
	}
}
