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

import org.easymock.Capture;

import to.sven.androidrccar.client.logic.handler.BatteryPowerMessageHandler;
import to.sven.androidrccar.client.logic.handler.CameraConnectionParameterMessageHandler;
import to.sven.androidrccar.client.logic.handler.FeatureMessageHandler;
import to.sven.androidrccar.client.logic.handler.LocationMessageHandler;
import to.sven.androidrccar.client.service.contract.IClientConfigurationService;
import to.sven.androidrccar.common.communication.model.CyclicUpdateFeatureType;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.RequestCameraMessage;
import to.sven.androidrccar.common.communication.model.SetUpCyclicUpdateMessage;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link FeatureMessageHandler}.
 * @author sven
 *
 */
@UsesMocks(IClientConfigurationService.class)
public class FeatureMessageHandlerTest extends
		AbstractTestCaseForClientMessageHandler<FeatureMessageHandler> {

	/**
	 * Normal mock
	 * @see AndroidMock#createMock
	 */
	private IClientConfigurationService configMock;
	
	/**
	 * The {@link FeatureMessage} to handle.
	 */
	private FeatureMessage featureMessage;
	
	/**
	 * @see IClientConfigurationService#getCameraEnabled()
	 */
	private boolean clientCameraEnabled;
	
	/**
	 * In addition to {@link AbstractTestCaseForClientMessageHandler#setUp()}
	 * is set up also a mock for {@link IClientConfigurationService}.
	 */
	@Override
	protected void setUp() {
		super.setUp();
		
		configMock = AndroidMock.createMock(IClientConfigurationService.class);
				  
		AndroidMock.expect(cdcMock.getConfiguration())
		   		   .andReturn(configMock)
				   .anyTimes();

	}
	
	/**
	 * In addition to {@link AbstractTestCaseForClientMessageHandler#verifyFieldMocks()}
	 * is set up also verifies {@link #configMock}. 
	 */
	@Override
	protected void verifyFieldMocks() {
		super.verifyFieldMocks();
		AndroidMock.verify(configMock);
	}
	
	/**
	 * Configures the part that is needed by all tests.
	 * You need to set {@link #featureMessage} 
	 * and {@link #clientCameraEnabled} before.
	 */
	public void universalConfigTest() {
		AndroidMock.expect(configMock.getCameraEnabled())
				   .andReturn(clientCameraEnabled)
				   .anyTimes();
		
		logicMock.clearMessageHandlers();
		cdcMock.setFeatures(AndroidMock.same(featureMessage));
	}
	
	/**
	 * Runs the test and verifies the mocks at the end.
	 */
	public void runAndVerifyTest() {
		AndroidMock.replay(configMock);
		FeatureMessageHandler handler = aHandlerForTest();
		handler.handleMessage(featureMessage);
		
		verifyFieldMocks();
	}
	
	/**
	 * Test for:
	 * {@link #testHandleMessage_noFeatures()}
	 * {@link #testHandleMessage_adjustableSpeed()}
	 * {@link #testHandleMessage_driveBackward()}
	 * 
	 * @param adjustableSpeed Supported?
	 * @param driveBackward Supported?
	 */
	private void genericTestHandleMessage_enableDriving(boolean adjustableSpeed, boolean driveBackward) {
		featureMessage = new FeatureMessage(false, 0, 0, 0, 0,
											adjustableSpeed, driveBackward,
											false, false, false, false);
		clientCameraEnabled = true;
		universalConfigTest();
		
		logicListenerMock.enableDriving(adjustableSpeed, driveBackward);
		
		runAndVerifyTest();
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * No features supported.
	 */
	public void testHandleMessage_noFeatures() {
		genericTestHandleMessage_enableDriving(false, false);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#adjustableSpeed} is supported.
	 */
	public void testHandleMessage_adjustableSpeed() {
		genericTestHandleMessage_enableDriving(true, false);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#driveBackward} is supported.
	 */
	public void testHandleMessage_driveBackward() {
		genericTestHandleMessage_enableDriving(false, true);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#batteryPower} is supported.
	 */
	public void testHandleMessage_batteryPower() {
		featureMessage = new FeatureMessage(false, 0, 0, 0, 0,
											false, false,
											false, false, false, true);
		clientCameraEnabled = false;
		universalConfigTest();
		
		logicMock.registerMessageHandler(BatteryPowerMessageHandler.class);
		AndroidMock.expect(configMock.getBatteryPowerMessageInterval())
				   .andReturn(60000);
		Capture<SetUpCyclicUpdateMessage> msgCapture = 
				new Capture<SetUpCyclicUpdateMessage>();
		logicMock.sendMessage(AndroidMock.capture(msgCapture));
		
		logicListenerMock.enableDriving(false, false);
		
		runAndVerifyTest();
		
		assertEquals(CyclicUpdateFeatureType.batteryPower, msgCapture.getValue().featureType);
		assertEquals(60000, msgCapture.getValue().interval);
	}

	/**
	 * Test for the following methods:  
	 * @param location {@link #testHandleMessage_location()}
	 * @param bearing {@link #testHandleMessage_bearing()}
	 * @param speed {@link #testHandleMessage_speed()}
	 */
	private void genericTestHandleMessage_anyLocationDependingFeature(
			boolean location, boolean bearing, boolean speed)
	{
		featureMessage = new FeatureMessage(false, 0, 0, 0, 0,
											false, false,
											location, bearing, speed,
											false);
		clientCameraEnabled = false;
		universalConfigTest();
		
		logicMock.registerMessageHandler(LocationMessageHandler.class);
		logicMock.enableLocationService();
		AndroidMock.expect(configMock.getLocationMessageInterval())
				   .andReturn(1000);
		Capture<SetUpCyclicUpdateMessage> msgCapture = 
				new Capture<SetUpCyclicUpdateMessage>();
		logicMock.sendMessage(AndroidMock.capture(msgCapture));
		
		logicListenerMock.enableDriving(false, false);
		
		runAndVerifyTest();
		
		assertEquals(CyclicUpdateFeatureType.location, msgCapture.getValue().featureType);
		assertEquals(1000, msgCapture.getValue().interval);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#location} is supported.
	 */
	public void testHandleMessage_location() {
		genericTestHandleMessage_anyLocationDependingFeature(true, false, false);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#bearing} is supported.
	 */
	public void testHandleMessage_bearing() {
		genericTestHandleMessage_anyLocationDependingFeature(false, true, false);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#speed} is supported.
	 */
	public void testHandleMessage_speed() {
		genericTestHandleMessage_anyLocationDependingFeature(false, false, true);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#camera} is supported but not wanted by client.
	 */
	public void testHandleMessage_cameraNotWanted() {
		featureMessage = new FeatureMessage(true, 0, 0, 0, 0,
											false, false,
											false, false, false, false);
		clientCameraEnabled = false;
		universalConfigTest();
		
		logicListenerMock.enableDriving(false, false);
		
		runAndVerifyTest();
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#camera} is supported and wanted by client.
	 */
	public void testHandleMessage_camera() {
		featureMessage = new FeatureMessage(true, 0, 0, 0, 0,
											false, false,
											false, false, false, false);
		clientCameraEnabled = true;
		universalConfigTest();
		
		logicMock.registerMessageHandler(CameraConnectionParameterMessageHandler.class);
		Capture<RequestCameraMessage> msgCapture = new Capture<RequestCameraMessage>();
		logicMock.sendMessage(AndroidMock.capture(msgCapture));
		
		logicListenerMock.enableDriving(false, false);
		
		runAndVerifyTest();
		
		assertEquals(true, msgCapture.getValue().activateCamera);
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#supportPanCamera()} is supported.
	 */
	public void testHandleMessage_cameraPan() {
		featureMessage = new FeatureMessage(true, 20, 80, 0, 0,
											false, false,
											false, false, false, false);
		clientCameraEnabled = true;
		universalConfigTest();
		
		logicMock.registerMessageHandler(CameraConnectionParameterMessageHandler.class);
		logicListenerMock.enablePan(20, 80);
		logicMock.sendMessage(AndroidMock.isA(RequestCameraMessage.class));
				
		logicListenerMock.enableDriving(false, false);
		
		runAndVerifyTest();
	}
	
	/**
	 * Tests {@link FeatureMessageHandler#handleMessage}:
	 * {@link FeatureMessage#supportTiltCamera()} is supported.
	 */
	public void testHandleMessage_cameraTilt() {
		featureMessage = new FeatureMessage(true, 0, 0, 30, 90,
											false, false,
											false, false, false, false);
		clientCameraEnabled = true;
		universalConfigTest();
		
		logicMock.registerMessageHandler(CameraConnectionParameterMessageHandler.class);
		logicListenerMock.enableTilt(30, 90);
		logicMock.sendMessage(AndroidMock.isA(RequestCameraMessage.class));
		
		logicListenerMock.enableDriving(false, false);
		
		runAndVerifyTest();
	}
	
	
}
