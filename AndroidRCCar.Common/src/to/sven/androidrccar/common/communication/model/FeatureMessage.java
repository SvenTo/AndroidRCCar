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
package to.sven.androidrccar.common.communication.model;

import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.LocationMessage;
import to.sven.androidrccar.common.communication.model.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This Message is send to the Client after authentication.
 * It contain information about the capabilities of the Host.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class FeatureMessage extends Message {
	
	/**
	 * Creates a new instance of {@link FeatureMessage}.
	 * @param camera {@link #camera}
	 * @param cameraPanMin {@link #cameraPanMin}
	 * @param cameraPanMax {@link #cameraPanMax}
	 * @param cameraTiltMin {@link #cameraTiltMin}
	 * @param cameraTiltMax {@link #cameraTiltMin}
	 * @param adjustableSpeed {@link #adjustableSpeed}
	 * @param driveBackward {@link #driveBackward}
	 * @param location {@link #location}
	 * @param bearing {@link #bearing}
	 * @param speed {@link #speed}
	 * @param batteryPower {@link #batteryPower}
	 */
	@JsonCreator
	public FeatureMessage(@JsonProperty("camera") boolean camera,
						  @JsonProperty("cameraPanMin") float cameraPanMin,
						  @JsonProperty("cameraPanMax") float cameraPanMax,
						  @JsonProperty("cameraTiltMin") float cameraTiltMin,
						  @JsonProperty("cameraTiltMax") float cameraTiltMax,
						  @JsonProperty("adjustableSpeed") boolean adjustableSpeed,
						  @JsonProperty("driveBackward") boolean driveBackward,
						  @JsonProperty("location") boolean location,
						  @JsonProperty("bearing") boolean bearing,
						  @JsonProperty("speed") boolean speed,
						  @JsonProperty("batteryPower") boolean batteryPower) {
		this.camera = camera;
		this.cameraPanMin = cameraPanMin;
		this.cameraPanMax = cameraPanMax;
		this.cameraTiltMin = cameraTiltMin;
		this.cameraTiltMax = cameraTiltMax; 
		this.adjustableSpeed = adjustableSpeed;
		this.driveBackward = driveBackward;
		this.location = location;
		this.bearing = bearing;
		this.speed = speed;
		this.batteryPower = batteryPower;
	}
	
	/**
	 * Can the Client get a stream of the video camera.
	 */
	public final boolean camera;
	
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
	 * Is the speed adjustable. If not the car can only drive with constant speed (or it is stopped.) 
	 */
	public final boolean adjustableSpeed;
	
	/**
	 * Can the Client get information about the geographic location (latitude, longitude and altitude) of the car.
	 */
	public final boolean location;
	
	/**
	 * Can the Client get information about the bearing of the car.
	 */
	public final boolean bearing;
	
	/**
	 * Can the Client get information about the speed of the car.
	 */
	public final boolean speed;
	
	/**
	 * Can the Client get information about the battery charging level of the car.
	 */
	public final boolean batteryPower;
	
	/**
	 * Can the car drive backward
	 */
	public final boolean driveBackward;
	
	/**
	 * Can the Client pan the camera (horizontal rotate).
	 * @return True, if supported.
	 */
	public boolean supportPanCamera() {
		return cameraPanMin != 0 && cameraPanMax != 0;
	}
	
	/**
	 * Can the Client tilt the camera (vertical rotate).
	 * @return True, if supported.
	 */
	public boolean supportTiltCamera() {
		return cameraTiltMin != 0 && cameraTiltMax != 0;
	}
	
	/**
	 * True, if {@link #supportTiltCamera} or {@link #supportPanCamera} (or both) is supported.
	 * @return True, if supported.
	 */
	public boolean supportRotateCamera() {
		return supportPanCamera() || supportTiltCamera();
	}
	
	/**
	 * True, if any of the information in {@link LocationMessage} is supported.
	 * @return True, if supported.
	 */
	public boolean supportAnyLocationDependingFeature() {
		return location || bearing || speed;
	}
}
