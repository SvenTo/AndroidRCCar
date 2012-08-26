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
package to.sven.androidrccar.host.accessorycommunication.contract;

import java.io.Closeable;

import to.sven.androidrccar.common.communication.model.AdjustSpeedMessage;
import to.sven.androidrccar.common.communication.model.RotateCameraMessage;
import to.sven.androidrccar.common.communication.model.TurnCarMessage;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;

/**
 * The interface to communicate with µController.
 * 
 * @author sven
 *
 */
public interface IAccessoryCommunication extends Closeable {
	
	/**
	 * Starts the communication thread, so we can send messages and receive the answer.
	 * @throws IllegalStateException If no listener was set with {@link #setListener}.
	 */
	void startCommunication() throws IllegalStateException;
	
	/**
	 * Returns the Features of the Car, that was send by the µController.
	 * @return {@link CarFeatures}
	 * @throws IllegalStateException Is thrown, before {@link IAccessoryCommunicationListener#connectionInitialized} is called.
	 * 								 (Until now we have no features received from µController.)
	 */
	CarFeatures getCarFeatures() throws IllegalStateException;
	
	/**
	 * Adjust the speed of the car.  
	 * @param speed {@link AdjustSpeedMessage#speed}
	 */
	void adjustSpeed(float speed);
	
	/**
	 * Change the rotation of the "steering wheel".
	 * @param rotation {@link TurnCarMessage#rotation}
	 */
	void turnCar(float rotation);
	
	/**
	 * Rotates the camera (if supported).
	 * @param pan {@link RotateCameraMessage#pan}
	 * @param tilt {@link RotateCameraMessage#tilt}
	 */
	void rotateCamera(float pan, float tilt);
	
	/**
	 * Tell the µController to send information about the battery state.
	 */
	void requestBatteryState();
	
	/**
	 * Sets the Listener that want to now, when something happens here.
	 * It is possible to change it every time.
	 * @param listener {@link IAccessoryCommunicationListener}
	 * @throws IllegalArgumentException If {@code listener} is null.
	 */
	void setListener(IAccessoryCommunicationListener listener);
}
