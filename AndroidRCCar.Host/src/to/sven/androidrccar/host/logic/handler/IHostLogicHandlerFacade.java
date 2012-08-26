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
package to.sven.androidrccar.host.logic.handler;

import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.common.logic.handler.ILogicHandlerFacade;
import to.sven.androidrccar.host.framework.IHostDependencyContainer;
import to.sven.androidrccar.host.logic.impl.BatteryPowerCyclicTask;
import to.sven.androidrccar.host.logic.impl.HostLogic;
import to.sven.androidrccar.host.logic.impl.ICyclicTask;
import to.sven.androidrccar.host.logic.impl.LocationCyclicTask;

/**
 * Interface for the {@link AbstractHostMessageHandler}s to access the {@link HostLogic}. 
 * @author sven
 *
 */
public interface IHostLogicHandlerFacade extends
		ILogicHandlerFacade<IHostDependencyContainer> {
	/**
	 * Generated salt for authentication.
	 * @return A salt
	 * @see GreetingMessage#authSalt
	 */
	String getSalt();
	
	/**
	 * Gets the {@link BatteryPowerCyclicTask}.
	 * @return {@link BatteryPowerCyclicTask}
	 */
	ICyclicTask getBatteryPowerCyclicTask();

	/**
	 * Gets the {@link LocationCyclicTask}.
	 * @return {@link LocationCyclicTask}
	 */
	ICyclicTask getLocationCyclicTask();
}
