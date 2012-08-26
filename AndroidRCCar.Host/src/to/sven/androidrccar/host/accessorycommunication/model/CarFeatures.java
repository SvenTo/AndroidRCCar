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
package to.sven.androidrccar.host.accessorycommunication.model;


/**
 * This class contain the information, what features supported by the car.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class CarFeatures {
	
	/**
	 * Creates a new {@link CarFeatures}
	 * @param cameraPanMin {@link #cameraPanMin}
	 * @param cameraPanMax {@link #cameraPanMax}
	 * @param cameraTiltMin {@link #cameraTiltMin}
	 * @param cameraTiltMax {@link #cameraTiltMax}
	 * @param adjustableSpeed {@link #adjustableSpeed}
	 * @param driveBackward {@link #driveBackward}
	 * @param batteryPower {@link #batteryPower}
	 */
	public CarFeatures(float cameraPanMin,
					   float cameraPanMax,
					   float cameraTiltMin,
					   float cameraTiltMax,
					   boolean adjustableSpeed,
					   boolean driveBackward,
					   boolean batteryPower) {
		this.cameraPanMin = cameraPanMin;
		this.cameraPanMax = cameraPanMax;
		this.cameraTiltMin = cameraTiltMin;
		this.cameraTiltMax = cameraTiltMax;
		this.adjustableSpeed = adjustableSpeed;
		this.driveBackward = driveBackward;
		this.batteryPower = batteryPower;
	}
	
	/**
	 * The maximum pan of the camera to the right.
	 * Possible values are 0-180 degree.
	 * 0 degree is looking forward.
	 * (If it don't support right pan, the value is 0.)
	 */
	public final float cameraPanMax;
	
	/**
	 * The maximum pan of the camera to the left.
	 * Possible values are 0-180 degree.
	 * 0 degree is looking forward.
	 * (If it don't support left pan, the value is 0.)
	 */
	public final float cameraPanMin;
	
	/**
	 * The maximum tilt of the camera up.
	 * Possible values are 0-180 degree.
	 * (0 degree is looking forward.)
	 * (If it don't support up tilt, the value is 0.)
	 */
	public final float cameraTiltMax;
	
	/**
	 * The maximum tilt of the camera down.
	 * Possible values are 0-180 degree.
	 * 0 degree is looking forward.
	 * (If it don't support down tilt, the value is 0.)
	 */
	public final float cameraTiltMin;
	
	/**
	 * Is the speed adjustable. If not the car can only drive with constant speed. 
	 */
	public final boolean adjustableSpeed;
	
	/**
	 * Car provides information about remaining battery power of the car.
	 */
	public final boolean batteryPower;
	
	/**
	 * Car can drive backward.
	 */
	public final boolean driveBackward;
	
	/**
	 * Car supports panning of the camera (horizontal rotate).
	 * @see CarFeatures#cameraTiltMin
	 * @see CarFeatures#cameraTiltMax
	 * @return True, if supported.
	 */
	public boolean supportPanCamera() {
		return cameraPanMin != 0 || cameraPanMax != 0;
	}
	
	/**
	 * Car supports tilting of the camera (vertical rotate).
	 * @see CarFeatures#cameraPanMin
	 * @see CarFeatures#cameraPanMax
	 * @return True, if supported.
	 */
	public boolean supportTiltCamera() {
		return cameraTiltMin != 0 || cameraTiltMax != 0;
	}
}
