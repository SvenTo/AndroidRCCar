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
package to.sven.androidrccar.client.test.logic.handler;

import to.sven.androidrccar.client.logic.handler.LocationMessageHandler;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.LocationMessage;
import to.sven.androidrccar.common.service.contract.ILocationService;

import android.location.Location;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link LocationMessageHandler}.
 * It also tests the behavior of the {@link LocationMessage}.
 * @author sven
 *
 */
@UsesMocks({ ILocationService.class })
public class LocationMessageHandlerTest extends AbstractTestCaseForClientMessageHandler<LocationMessageHandler> {
	
	/**
	 * Is {@link FeatureMessage#location} supported for the test?
	 */
	private boolean location;

	/**
	 * Is  {@link FeatureMessage#bearing} supported for the test?
	 */
	private boolean bearing;
	
	/**
	 * Is  {@link FeatureMessage#speed} supported for the test?
	 */
	private boolean speed;
	
	/**
	 * The features actual supported in the tests.
	 */
	private FeatureMessage features;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private ILocationService locationServiceMock;
	
	/**
	 * In addition to {@link AbstractTestCaseForClientMessageHandler#setUp()}
	 * is set up also a mock for {@link ILocationService}.
	 */
	@Override
	protected void setUp() {
		super.setUp();
		
		locationServiceMock = AndroidMock.createStrictMock(ILocationService.class);

		AndroidMock.expect(cdcMock.getLocationService())
		   		   .andReturn(locationServiceMock)
				   .anyTimes();
	}
	
	/**
	 * Creates a new {@link FeatureMessage} and adds it to the {@link #cdcMock}.
	 */
	private void initFeatures() {
		features = new FeatureMessage(false, 0, 0, 0, 0,
									 false, false,
					   				 location, bearing, speed,
						  			 false);
		AndroidMock.expect(cdcMock.getFeatures())
				   .andReturn(features)
				   .anyTimes();
	}
	
	/**
	 * Creates the {@link LocationMessageHandler}
	 * and calls {@link LocationMessageHandler#handleMessage}. 
	 * @param androidLocation The mock remote device location (Host).
	 */
	private void runTest(Location androidLocation) {
		AndroidMock.replay(locationServiceMock);
		LocationMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new LocationMessage(androidLocation, 
												  location,
												  bearing,
												  speed));
	}
	
	/**
	 * In addition to {@link AbstractTestCaseForClientMessageHandler#verifyFieldMocks()}
	 * is set up also verifies {@link #locationServiceMock}.
	 */
	@Override
	protected void verifyFieldMocks() {
		super.verifyFieldMocks();
		AndroidMock.verify(locationServiceMock);
	}
	
	/**
	 * Mocks the location of this device (Client).
	 */
	private void initMyLocation() {
		Location myLocation = new Location((String)null);
		myLocation.setLatitude(52.673978d);
		myLocation.setLongitude(8.802951d); 
		AndroidMock.expect(locationServiceMock.getCurrentLocation())
				   .andReturn(myLocation)
				   .anyTimes();
	}
	
	/**
	 * Sets the mock location of the remote device (Host).
	 * @param remoteLocation The object where its set.
	 */
	private void setLatLong(Location remoteLocation) {
		remoteLocation.setLatitude(53.055557d);
		remoteLocation.setLongitude(8.783387d);
	}
	
	/**
	 * Tests {@link LocationMessageHandler#handleMessage}:
	 * Everything is transmitted.
	 */
	public void testHandleMessage_All() {
		// Configure Test
		location = true;
		bearing = true;
		speed = true;
		initFeatures();
		initMyLocation();
		
		// Looking from myLocation to remoteLocation:
		// 42,45 +-0,1 km distance, -2° +-1 bearing
		logicListenerMock.setDistance(AndroidMock.eq(42.45f*1000, 100), AndroidMock.eq(-2f, 1f));
		logicListenerMock.setBearing(45);
		logicListenerMock.setSpeed(10);
		
		// Run Test
		Location remoteLocation = new Location((String)null);
		setLatLong(remoteLocation);
		remoteLocation.setBearing(45);
		remoteLocation.setSpeed(10);
		runTest(remoteLocation);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link LocationMessageHandler#handleMessage}:
	 * Only location is transmitted.
	 */
	public void testHandleMessage_Location() {
		// Configure Test
		location = true;
		bearing = true;
		speed = true;
		initFeatures();
		initMyLocation();
		
		// Looking from myLocation to remoteLocation:
		// 42,45 +-0,1 km distance, -2° +-1 bearing
		logicListenerMock.setDistance(AndroidMock.eq(42.45f*1000, 100), AndroidMock.eq(-2f, 1f));
		
		// Run Test
		Location remoteLocation = new Location((String)null);
		setLatLong(remoteLocation);
		runTest(remoteLocation);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link LocationMessageHandler#handleMessage}:
	 * Only bearing is transmitted.
	 */
	public void testHandleMessage_BearingOnly() {
		// Configure Test
		location = false;
		bearing = true;
		speed = false;
		initFeatures();
		
		logicListenerMock.setBearing(42);
		
		// Run Test
		Location remoteLocation = new Location((String)null);
		remoteLocation.setBearing(42);
		runTest(remoteLocation);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link LocationMessageHandler#handleMessage}:
	 * Only speed is transmitted.
	 */
	public void testHandleMessage_SpeedOnly() {
		// Configure Test
		location = false;
		bearing = false;
		speed = true;
		initFeatures();
		
		logicListenerMock.setSpeed(40);
		
		// Run Test
		Location androidLocation = new Location((String)null);
		androidLocation.setSpeed(40);
		runTest(androidLocation);
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Test if {@link LocationMessage#altitude}
	 * and {@link LocationMessage#accuracy} is correctly converted.
	 * (These are the only fields that not implicit tested by the other methods.)
	 */
	public void testToAndroidLocation() {
		Location remoteLocation = new Location((String)null);
		remoteLocation.setAltitude(33);
		remoteLocation.setAccuracy(42);
		
		LocationMessage locationMessage = 
				new LocationMessage(remoteLocation, true, false, false);
		Location actual = locationMessage.toAndroidLocation();
		assertEquals(33d, actual.getAltitude());
		assertEquals(42f, actual.getAccuracy());
	}
	
	// TODO: Test security! (constructor: sendX)
}
