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
import android.os.Looper;

/**
 * The {@link IDependencyContainer} contains all dependences for the {@link AbstractLogic}
 * and it's dependences.
 * 
 * @author sven 
 * @param <TFactory> The interface of the concrete {@link IFactory}
 * @param <TListener> The interface of the concrete {@link ILogicListener}.
 */
public abstract class AbstractDependencyContainer<TFactory extends IFactory,
												  TListener extends ILogicListener> implements IDependencyContainer<TFactory, TListener> {
	
	/**
	 * The connection to the Client/Host.
	 */
	private Socket socket;
	
	/**
	 * A listener for the Logic.
	 */
	private final TListener listener;
	
	/**
	 * A factory for object creation.
	 */
	private final TFactory factory;
	
	/**
	 * A {@link Handler} associated with the applications main thread (the one with the activities).
	 */
	private final Handler handler;
	
	/**
	 * If set, it contains the {@link ILocationService}.
	 */
	private ILocationService locationService;

	/**
	 * Contains the {@link Context} of the application.
	 */
	private final Context context;
	
	/**
	 * Default Constructor. 
	 * @param factory A factory for object creation.
	 * @param listener The one that that want to know, when something happen in the Logic (Most likely an Activity)
	 * @param context {@link Context}
	 */
	public AbstractDependencyContainer(TFactory factory, TListener listener, Context context) {
		this.listener = listener;
		this.factory = factory;
		this.context = context;
		this.handler = new Handler(Looper.getMainLooper()); // Note: Passing the looper shouldn't necessary,
															//		 but it makes it clear.
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Socket getSocket() {
		return socket;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TListener getLogicListener() {
		return listener;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TFactory getFactory() {
		return factory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Handler getHandler() {
		return handler;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Context getContext() {
		return context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocationService getLocationService() {
		return locationService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLocationService(ILocationService locationService) {
		this.locationService = locationService;
	}
}
