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

import to.sven.androidrccar.common.communication.model.CyclicUpdateFeatureType;
import to.sven.androidrccar.common.communication.model.Message;
import to.sven.androidrccar.common.communication.model.SetUpCyclicUpdateMessage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message is send to the host.
 * Set up a cyclic update from the host of a feature in {@link CyclicUpdateFeatureType}.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class SetUpCyclicUpdateMessage extends Message {
	
	/**
	 * Creates a new instance of {@link SetUpCyclicUpdateMessage}.
	 * @param featureType {@link #featureType}
	 * @param interval {@link #interval}
	 */
	@JsonCreator
	public SetUpCyclicUpdateMessage(@JsonProperty("featureType") CyclicUpdateFeatureType featureType,
									@JsonProperty("interval") int interval) {
		this.featureType = featureType;
		this.interval = interval;
	}
	
	/**
	 * Send updates of what.
	 */
	public final CyclicUpdateFeatureType featureType;
	
	/**
	 * Send updates every x ms.
	 * If interval is =< 0 no updates will be send.
	 */
	public final int interval;
	
	/**
	 * Indicates, if the cyclic update should be enabled or disabled.
	 * @return True, if it should be enabled.
	 */
	public boolean enableCyclicUpdate() {
		return interval > 0;
	}
}
