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

import to.sven.androidrccar.host.accessorycommunication.contract.IAccessoryCommunication;
import android.os.Handler;

/**
 * 
 * @author sven
 *
 */
public class BatteryPowerCyclicTask extends AbstractCyclicTask {

	/**
	 * {@link IAccessoryCommunication}
	 */
	private final IAccessoryCommunication accessoryCommunication;
	
	/**
	 * Default constructor.
	 * @param accessoryCommunication {@link IAccessoryCommunication} 
	 * @param handler A {@link Handler} that is used to call the task after the given {@code interval}.
	 */
	public BatteryPowerCyclicTask(IAccessoryCommunication accessoryCommunication, Handler handler) {
		super(handler);
		this.accessoryCommunication = accessoryCommunication; 
	}

	/**
	 * Tells the {@link IAccessoryCommunication} to get a update for the battery state.
	 */
	@Override
	public void runTask() {
		accessoryCommunication.requestBatteryState();
	}

}
