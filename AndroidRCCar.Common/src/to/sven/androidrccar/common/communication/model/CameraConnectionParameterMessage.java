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

import to.sven.androidrccar.common.communication.model.CameraConnectionParameterMessage;
import to.sven.androidrccar.common.communication.model.Message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This message contains the parameters for the access to the camera stream.
 * The parameters can converted to a Uniform Resource Locator (http://tools.ietf.org/html/rfc3986).
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class CameraConnectionParameterMessage extends Message {
	
	/**
	 * Creates a new instance of {@link CameraConnectionParameterMessage}.
	 * @param scheme {@link #scheme}
	 * @param port {@link #port}
	 * @param path {@link #path}
	 */
	@JsonCreator
	public CameraConnectionParameterMessage(@JsonProperty("port") int port,
											@JsonProperty("protocol") String scheme,
											@JsonProperty("path") String path) {
		this.port = port;
		this.scheme = scheme;
		this.path = path;
	}
	
	/**
	 * Port to the stream
	 */
	public final int port;
	
	/**
	 * A URI scheme
	 * Note: Currently only RTSP is supported.
	 */
	public final String scheme;
	
	/**
	 * Path to the camera video stream.
	 * (without beginning: /)
	 */
	public final String path;
	
	// TODO: Test this?:
	
	/**
	 * Creates a URL with from the message.
	 * @param host The hostname/IP-address of the host
	 * @return A URL for connecting to the camera stream.
	 */
	public String toURL(String host) {
		return scheme + "://" + host + ":" + port + "/" + path;
	}
}
