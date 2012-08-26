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
package to.sven.androidrccar.common.model;

import java.net.URLEncoder;

import android.net.UrlQuerySanitizer;

/**
 * Data object that contains the information for connecting to the Host.
 * 
 * The instances of this class are immutable.
 * @author sven
 *
 */
public class ConnectionParameter {
	
	/**
	 * Default port used by the host
	 */
	public static final int DEFAULT_PORT = 4253; 
	
	/**
	 * The base URL, that is used to receive intents from other Applications
	 * (like the user click on the link in a mail).
	 */
	private static final String URL_BASE = "http://androidrccar.sven.to/connect/";
	
	/**
	 * The complete URL with parameters for formating.
	 * @see String#format(String, Object...)
	 */
	private static final String URL_FORMAT = URL_BASE+"?host=%1$s&port=%2$d&password=%3$s";
	
	/**
	 * Creates a new instance of {@link ConnectionParameter}
	 * @param host {@link #host}
	 * @param port {@link #port}
	 * @param password {@link #password}
	 */
	public ConnectionParameter(String host, int port,  String password) {
		this.host = host;
		this.port = port;
		this.password = password;
	}
	
	/**
	 * Builds the connection parameters from a connection URL, like it can receive in a intent.
	 * @param connectionURL connection parameters encoded in a URL
	 */
	public ConnectionParameter(String connectionURL) {
		UrlQuerySanitizer query = new UrlQuerySanitizer(connectionURL);
		
		this.host = query.getValue("host");
		this.port = Integer.parseInt(query.getValue("port"));
		this.password = query.getValue("password");
	}
	
	/**
	 * Creates a new instance of {@link ConnectionParameter}.
	 * @param hostWithPort A string that contains the {@link #host} and optionally the {@link #port}
	 * 					   delimited by ':' from host. 
	 * 					   If no port is given, the ({@link #DEFAULT_PORT}) is used.
	 * @param password {@link #password}
	 */
	public ConnectionParameter(String hostWithPort, String password) {
		int portDelmitter = hostWithPort.indexOf(':');
		if(portDelmitter == -1) {
			host = hostWithPort;
			port = DEFAULT_PORT;
		} else {
			host = hostWithPort.substring(0, portDelmitter);
			port = Integer.parseInt(hostWithPort.substring(portDelmitter+1));
		}
		this.password = password;
	}
	
	/**
	 * IP/Hostname of the Host
	 */
	public final String host;
	
	/**
	 * Port where the Host application listen.
	 */
	public final int port;
	
	/**
	 * Password, for authentication on the host.
	 */
	public final String password;
	
	/**
	 * Returns the IP/Hostname with port (if it's not the default port).
	 */
	@Override
	public String toString() {
		return host+((port != DEFAULT_PORT)?":"+port:"");
	}
	
	/**
	 * Returns the connection parameters as a URL
	 * that can received by client activity intent filter
	 * to connect to the host.
	 * @return Formatted {@link #URL_FORMAT}. 
	 */
	public String toConnectionURL() {
		return String.format(URL_FORMAT, URLEncoder.encode(host), new Integer(port), URLEncoder.encode(password));
	}
}
