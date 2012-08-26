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
package to.sven.androidrccar.host.logic.impl;

import android.location.Location;
import to.sven.androidrccar.common.communication.model.LocationMessage;
import to.sven.androidrccar.common.service.contract.ILocationService;
import to.sven.androidrccar.host.logic.handler.IHostLogicHandlerFacade;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;

/**
 * Sends cyclic updates about the location of the device.
 * @author sven
 *
 */
public class LocationCyclicTask extends AbstractCyclicTask {
	/**
	 * The {@link IHostLogicHandlerFacade}.
	 */
	private final IHostLogicHandlerFacade facade;
	
	/**
	 * Default Constructor.
	 * @param facade The {@link IHostLogicHandlerFacade}.
	 */
	public LocationCyclicTask(IHostLogicHandlerFacade facade) {
		super(facade.getDependency().getHandler());
		this.facade = facade; 
	}

	/**
	 * Send the new location, 
	 * if the location has changed. 
	 */
	@Override
	public void runTask() {
		ILocationService locationService = this.facade
											   .getDependency()
											   .getLocationService();
		if(locationService.hasLocationChanged()) {
			Location location = locationService.getCurrentLocation();
			if(location != null) {
				IHostConfigurationService config = facade.getDependency().getConfiguration();
				facade.sendMessage(new LocationMessage(location,
													   config.shareLocation(),
													   config.shareSpeed(),
													   config.shareBearing()));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * And enable location service on start.
	 */
	@Override
	public void start() {
		if(!isRunning()) {
			facade.enableLocationService();
		}
		super.start();
	}
}
