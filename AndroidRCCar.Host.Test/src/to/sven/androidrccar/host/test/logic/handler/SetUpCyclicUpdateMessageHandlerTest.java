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
package to.sven.androidrccar.host.test.logic.handler;

import java.io.IOException;

import to.sven.androidrccar.common.communication.model.CyclicUpdateFeatureType;
import to.sven.androidrccar.common.communication.model.SetUpCyclicUpdateMessage;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.logic.handler.RotateCameraMessageHandler;
import to.sven.androidrccar.host.logic.handler.SetUpCyclicUpdateMessageHandler;
import to.sven.androidrccar.host.logic.impl.ICyclicTask;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link SetUpCyclicUpdateMessageHandler}.
 * @author sven
 *
 */
@UsesMocks(ICyclicTask.class)
public class SetUpCyclicUpdateMessageHandlerTest extends
		AbstractTestCaseForHostMessageHandler<SetUpCyclicUpdateMessageHandler> {
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private ICyclicTask taskMock;
	
	/**
	 * In addition to {@link AbstractTestCaseForHostMessageHandler#setUp()}
	 * is set up also a mock for {@link ICyclicTask}.
	 */
	@Override
	protected void setUp() {
		super.setUp();
		taskMock = AndroidMock.createStrictMock(ICyclicTask.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void replayFieldMocks() {
		super.replayFieldMocks();
		AndroidMock.replay(taskMock);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void verifyFieldMocks() {
		super.verifyFieldMocks();
		AndroidMock.verify(taskMock);
	}
	
	/**
	 * Tests {@link RotateCameraMessageHandler#handleMessage}.
	 * Request updates for battery power and it's not running. 
	 */
	public void testHandleMessage_runTask_isNotRunning() {
		// Configure Test
		configureCarFeatures();
		AndroidMock.expect(logicMock.getBatteryPowerCyclicTask())
				   .andReturn(taskMock);
		taskMock.setInterval(1000);
		AndroidMock.expect(taskMock.isRunning())
				   .andReturn(false);
		taskMock.start();
		
		// Run Test
		SetUpCyclicUpdateMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.batteryPower, 1000));
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link RotateCameraMessageHandler#handleMessage}.
	 * Request updates for battery power and it's running. 
	 */
	public void testHandleMessage_run_isRunning() {
		// Configure Test
		configureCarFeatures();
		AndroidMock.expect(logicMock.getBatteryPowerCyclicTask())
				   .andReturn(taskMock);
		taskMock.setInterval(1100);
		AndroidMock.expect(taskMock.isRunning())
				   .andReturn(true);
		
		// Run Test
		SetUpCyclicUpdateMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.batteryPower, 1100));
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link RotateCameraMessageHandler#handleMessage}.
	 * Request no updates for battery power. 
	 */
	public void testHandleMessage_stop() {
		// Configure Test
		configureCarFeatures();
		AndroidMock.expect(logicMock.getBatteryPowerCyclicTask())
				   .andReturn(taskMock);
		taskMock.stop();
		
		// Run Test
		SetUpCyclicUpdateMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.batteryPower, 0));
		
		// Verify Test
		verifyFieldMocks();
	}

	/**
	 * Put {@link CarFeatures} into the dependency container.
	 */
	private void configureCarFeatures() {
		CarFeatures features = new CarFeatures(0, 0, 0, 0, false, false, true);
		AndroidMock.expect(hdcMock.getCarFeatures())
				   .andReturn(features)
				   .anyTimes();
	}
	
	/**
	 * Tests {@link RotateCameraMessageHandler#handleMessage}.
	 * Request updates for location and it's not running. 
	 */
	public void testHandleMessage_location() {
		// Configure Test
		IHostConfigurationService configMock = AndroidMock.createStrictMock(IHostConfigurationService.class);
		AndroidMock.expect(hdcMock.getConfiguration())
				   .andReturn(configMock)
				   .anyTimes();
		AndroidMock.expect(configMock.shareAnyLocationDependingFeature())
		   		   .andReturn(true);
		
		AndroidMock.expect(logicMock.getLocationCyclicTask())
				   .andReturn(taskMock);
		taskMock.setInterval(1000);
		AndroidMock.expect(taskMock.isRunning())
				   .andReturn(false);
		taskMock.start();
		
		// Run Test
		AndroidMock.replay(configMock);
		SetUpCyclicUpdateMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.location, 1000));
		
		// Verify Test
		verifyFieldMocks();
		AndroidMock.verify(configMock);
	}

	/**
	 * Tests {@link SetUpCyclicUpdateMessageHandler#validateFeatureEnabled}:
	 * Request battery but it's not supported.
	 */
	public void validateFeatureEnabled_Battery() {
		// Configure Test
		CarFeatures features = new CarFeatures(0, 0, 0, 0, false, false ,false);
		AndroidMock.expect(hdcMock.getCarFeatures())
				   .andReturn(features)
				   .anyTimes();
		logicMock.handleError(AndroidMock.isA(IOException.class));
		
		// Run Test
		SetUpCyclicUpdateMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.batteryPower, 1000));
		
		// Verify Test
		verifyFieldMocks();
	}

	/**
	 * Tests {@link SetUpCyclicUpdateMessageHandler#validateFeatureEnabled}:
	 * Request location but it's not supported.
	 */
	@UsesMocks(IHostConfigurationService.class)
	public void validateFeatureEnabled_Location() {
		// Configure Test
		IHostConfigurationService configMock = AndroidMock.createStrictMock(IHostConfigurationService.class);
		AndroidMock.expect(hdcMock.getConfiguration())
				   .andReturn(configMock)
				   .anyTimes();
		AndroidMock.expect(configMock.shareAnyLocationDependingFeature())
		   .andReturn(false);
		logicMock.handleError(AndroidMock.isA(IOException.class));

		// Run Test
		AndroidMock.replay(configMock);
		SetUpCyclicUpdateMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new SetUpCyclicUpdateMessage(CyclicUpdateFeatureType.location, 1000));
		
		// Verify Test
		AndroidMock.verify(configMock);
		verifyFieldMocks();
	}
}
