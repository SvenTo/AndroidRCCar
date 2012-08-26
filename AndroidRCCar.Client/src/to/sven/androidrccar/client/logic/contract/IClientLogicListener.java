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
package to.sven.androidrccar.client.logic.contract;

import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.common.communication.model.BatteryPowerMessage;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.logic.contract.ILogicListener;
import android.app.Activity;

/**
 * The {@link ClientLogic} uses this listener to inform the implementor of this interface 
 * that something happens. (Usually an {@link Activity})
 * @author sven
 *
 */
public interface IClientLogicListener extends ILogicListener {
	
	/**
	 * The authentication on the Host failed.
	 * @param reason Why the authentication failed. 
	 */
	void authenticationFalied(String reason);
	
	// TODO: das nur zwischenspeichern und bei enable driving abarbeiten?:
	
	/**
	 * Let the user pan the camera. 
	 * @param min {@link FeatureMessage#cameraPanMin}
	 * @param max {@link FeatureMessage#cameraPanMax}
	 */
	void enablePan(float min, float max);
	
	/**
	 * Let the user tilt the camera. 
	 * @param min {@link FeatureMessage#cameraTiltMin}
	 * @param max {@link FeatureMessage#cameraTiltMax}
	 */
	void enableTilt(float min, float max);
	
	/**
	 * The user can start driving the car.
	 * @param adjustableSpeed {@link FeatureMessage#adjustableSpeed}
	 * @param driveBackward {@link FeatureMessage#driveBackward}
	 */
	void enableDriving(boolean adjustableSpeed, boolean driveBackward);
	
	/**
	 * Show the given Battery charging level
	 * @param chargingLevel {@link BatteryPowerMessage#chargingLevel}
	 */
	void setBatteryPower(float chargingLevel);
	
	/**
	 * Show the new distance and bearing TO(!) the car in meters.
	 * ("Looking to the car")
	 * @param distance Distance to the car in meters.
	 * @param bearingTo Bearing TO(!) the car in degrees East of true North. (-180° to 180°) // TODO: Verify this
	 */
	void setDistance(float distance, float bearingTo);
	
	/**
	 * Bearing OF(!) the car.
	 * ("Where the car looks")
	 * @param bearing Bearing OF(!) the car in degrees East of true North.
	 */
	void setBearing(float bearing);
	
	/**
	 * Set speed of the car in meter per second.
	 * @param speed The speed
	 */
	void setSpeed(float speed);
}
