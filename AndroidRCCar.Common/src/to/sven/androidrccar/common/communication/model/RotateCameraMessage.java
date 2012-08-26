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
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.communication.model.RotateCameraMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Rotate the camera (if supported).
 * This Message is send to the host.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class RotateCameraMessage extends Message {
	
	/**
	 * Creates a new instance of {@link RotateCameraMessage}.
	 * @param pan {@link #pan}
	 * @param tilt {@link #tilt}
	 */
	@JsonCreator
	public RotateCameraMessage(@JsonProperty("pan") float pan, @JsonProperty("tilt") float tilt) {
		this.pan = pan;
		this.tilt = tilt;
	}
	
	/**
	 * Pan the camera (horizontal rotate).
	 * Possible values (in degree) depending on
	 * {@link FeatureMessage#cameraPanMin} and
	 * {@link FeatureMessage#cameraPanMax}.
	 * 0 degree is looking forward.
	 * If only tilt is supported, this value should be 0.
	 */
	public final float pan;
	
	/**
	 * Tilt the camera (vertical rotate).
	 * Possible values (in degree) depending on
	 * {@link FeatureMessage#cameraTiltMin} and
	 * {@link FeatureMessage#cameraTiltMax}.
	 * 0 degree is looking forward.
	 * If only pan is supported, this value should be 0.
	 */
	public final float tilt;
}
