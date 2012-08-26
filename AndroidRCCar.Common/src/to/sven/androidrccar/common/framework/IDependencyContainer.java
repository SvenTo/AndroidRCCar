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
package to.sven.androidrccar.common.framework;

import java.net.Socket;

import to.sven.androidrccar.common.logic.contract.ILogicListener;
import to.sven.androidrccar.common.logic.impl.AbstractLogic;
import to.sven.androidrccar.common.service.contract.ILocationService;
import android.content.Context;
import android.os.Handler;

/**
 * The {@link IDependencyContainer} contains all dependences for the {@link AbstractLogic}
 * and it's dependences.
 * 
 * @author sven 
 * @param <TFactory> The interface of the concrete {@link IFactory}
 * @param <TListener> The interface of the concrete {@link ILogicListener}.
 */
public interface IDependencyContainer<TFactory extends IFactory, TListener extends ILogicListener> {

	/**
	 * Returns the connection to the Host/Client.
	 * @return A {@link Socket}
	 */
	Socket getSocket();
	
	/**
	 * Sets the connection to the Host/Client.
	 * @param socket A {@link Socket}
	 */
	void setSocket(Socket socket);

	/**
	 * Returns the concrete {@link ILogicListener}.
	 * @return A concrete {@link ILogicListener}
	 */
	TListener getLogicListener();

	/**
	 * A factory that creates object for the logic.
	 * @return The factory.
	 */
	TFactory getFactory();

	/**
	 * Get a {@link Handler} associated with the applications main thread (the one with the activities).
	 * It can be used to communicate back with the main thread activities.
	 * @return A {@link Handler}
	 */
	Handler getHandler();
	
	/**
	 * Returns the {@link ILocationService}
	 * if it was set by {@link #setLocationService}.
	 * @return {@link ILocationService}
	 */
	ILocationService getLocationService();
	
	/**
	 * {@link Context} of the application
	 * @return {@link Context}
	 */
	Context getContext();

	/**
	 * Sets the {@link ILocationService}.
	 * @param locationService {@link ILocationService}
	 */
	void setLocationService(ILocationService locationService);
}
