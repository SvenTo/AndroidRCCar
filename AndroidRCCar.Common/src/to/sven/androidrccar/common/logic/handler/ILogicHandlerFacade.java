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
package to.sven.androidrccar.common.logic.handler;

import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.framework.AbstractDependencyContainer;
import to.sven.androidrccar.common.framework.IDependencyContainer;
import to.sven.androidrccar.common.framework.IFactory;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import to.sven.androidrccar.common.service.impl.LocationService;

/**
 * Interface for the {@link IMessageHandler}s to access the Logic ({@link AbstractLogic}).
 * @author sven
 *
 * @param <TDependencyContainer> The Container that contains the dependences.
 */
public interface ILogicHandlerFacade<TDependencyContainer extends IDependencyContainer<? extends IFactory, ?>> {
	
	/**
	 * Returns the concrete {@link AbstractDependencyContainer}
	 * @return concrete {@link AbstractDependencyContainer}
	 */
	TDependencyContainer getDependency();

	/**
	 * Register a new {@link IMessageHandler} that should handle messages.
	 * @param messageHandlerClass {@link Class} of {@link IMessageHandler} that should be added
	 * @param <T> Concrete Type of {@link IMessageHandler}
	 * 
	 */
	<T extends IMessageHandler<?>> void registerMessageHandler(Class<T> messageHandlerClass);
	
	/**
	 * Removes all registered {@link IMessageHandler}s.
	 */
	void clearMessageHandlers();
	
	/**
	 * Close the connection.
	 */
	void close();
	
	/**
	 * Sends a message to the remote Host/Client.
	 * @param message The message
	 */
	void sendMessage(Message message);
	
	/**
	 * Will be called on an error.
	 * Closes the connection and logs the Exception {@code ex}.
	 * @param ex The error
	 */
	void handleError(Exception ex);
	
	/**
	 * Will be called on an error.
	 * Closes the connection and logs the Exception {@code ex}.
	 * @param ex The error
	 * @param logMessage Additional message for logging
	 */
	void handleError(Exception ex, String logMessage);
	
	/**
	 * Creates a {@link LocationService} and sets it in the dependency container.
	 * (If not already set.)
	 */
	void enableLocationService();
}
