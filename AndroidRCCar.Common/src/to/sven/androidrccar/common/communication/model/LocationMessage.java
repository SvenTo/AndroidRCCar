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
import android.location.Location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Test this:

/**
 * This Message contains information about the location of the car.
 *   
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class LocationMessage extends Message {

	/**
	 * Creates a new instance of {@link LocationMessage}.
	 * @param latitude {@link #latitude}
	 * @param longitude {@link #longitude}
	 * @param hasAltitude {@link #hasAltitude}
	 * @param altitude {@link #altitude}
	 * @param hasAccuracy {@link #hasAccuracy}
	 * @param accuracy {@link #accuracy}
	 * @param hasBearing {@link #hasBearing}
	 * @param bearing {@link #bearing}
	 * @param hasSpeed {@link #hasSpeed}
	 * @param speed {@link #speed}
	 */
	@JsonCreator
	public LocationMessage(@JsonProperty("latitude") double latitude,
						   @JsonProperty("longitude") double longitude,
						   @JsonProperty("hasAltitude") boolean hasAltitude,
						   @JsonProperty("altitude") double altitude,
						   @JsonProperty("hasAccuracy") boolean hasAccuracy,
						   @JsonProperty("accuracy") float accuracy,
						   @JsonProperty("hasBearing") boolean hasBearing,
						   @JsonProperty("bearing") float bearing,
						   @JsonProperty("hasSpeed") boolean hasSpeed,
						   @JsonProperty("speed") float speed) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.hasAltitude = hasAltitude;
		this.altitude = altitude;
		this.hasAccuracy = hasAccuracy;
		this.accuracy = accuracy;
		this.hasBearing = hasBearing;
		this.bearing = bearing;
		this.hasSpeed = hasSpeed;
		this.speed = speed;
	}
	
	/**
	 * Creates a new {@link LocationMessage} from a {@link Location}.
	 * @param location An {@link Location}
	 * @param sendPosition Is it allowed to send the Position?
	 * @param sendSpeed Is it allowed to send the Speed?
	 * @param sendBearing Is it allowed to send the Bearing?
	 */
	public LocationMessage(Location location, boolean sendPosition,
						   boolean sendBearing, boolean sendSpeed) {
		this((sendPosition)?location.getLatitude():0,
		     (sendPosition)?location.getLongitude():0,
			 location.hasAltitude() && sendPosition,
			 (sendPosition)?location.getAltitude():0,
			 location.hasAccuracy() && sendPosition,
			 (sendPosition)?location.getAccuracy():0,
			 location.hasBearing() && sendBearing,
			 (sendBearing)?location.getBearing():0,
			 location.hasSpeed() && sendSpeed,
			 (sendSpeed)?location.getSpeed():0);
	}
	
	/**
	 * Contains this message information about accuracy?
	 */
	public final boolean hasAccuracy;
	
	/**
	 * Contains this message information about altitude?
	 */
	public final boolean hasAltitude;

	/**
	 * Contains this message information about the direction of travel.
	 */
	public final boolean hasBearing;
	
	/**
	 * Contains this message information about speed.
	 */
	public final boolean hasSpeed;
	
	/**
	 * Latitude of the location fix.
	 * If location is shared ({@link FeatureMessage#location})
	 * else 0.
	 */
	public final double latitude;
	
	/**
	 * Longitude of the location fix.
	 * If location is shared ({@link FeatureMessage#location})
	 * else 0.
	 */
	public final double longitude;
	
	/**
	 *  Altitude of the location fix.
	 *  If location is shared ({@link FeatureMessage#location})
	 *  and {@link #hasAltitude} else 0.
	 */
	public final double altitude;
	
	/**
	 * Accuracy of the location fix in meters.
	 *  If it's supported ({@link FeatureMessage#location})
	 *  and {@link #hasAccuracy} else 0.
	 */
	public final float accuracy;
	
	/**
	 * The direction of travel in degrees East of true North. // TODO: -180° to 180°?
	 *  If it's supported ({@link FeatureMessage#location})
	 *  and {@link #hasBearing} else 0.
	 */
	public final float bearing;
	
	/**
	 * Speed of the car over ground in meters/second.
	 */
	public final float speed;
	
	/**
	 * Converts the Location Message to {@link Location}.
	 * @return An {@link Location} with the information of this Message.
	 */
	public Location toAndroidLocation() {
		Location location = new Location((String)null);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		if(hasAltitude) {
			location.setAltitude(altitude);
		}
		if(hasAccuracy) {
			location.setAccuracy(accuracy);
		}
		if(hasBearing) {
			location.setBearing(bearing);
		}
		if(hasSpeed) {
			location.setSpeed(speed);
		}
		
		return location; 
	}
}
