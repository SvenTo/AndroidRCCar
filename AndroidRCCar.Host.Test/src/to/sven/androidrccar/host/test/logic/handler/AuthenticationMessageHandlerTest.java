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

import org.easymock.Capture;

import to.sven.androidrccar.common.communication.model.AuthenticationFailedMessage;
import to.sven.androidrccar.common.communication.model.AuthenticationMessage;
import to.sven.androidrccar.common.communication.model.FeatureMessage;
import to.sven.androidrccar.common.communication.model.GreetingMessage;
import to.sven.androidrccar.host.accessorycommunication.model.CarFeatures;
import to.sven.androidrccar.host.logic.contract.IHostLogicListener;
import to.sven.androidrccar.host.logic.handler.AdjustSpeedMessageHandler;
import to.sven.androidrccar.host.logic.handler.AuthenticationMessageHandler;
import to.sven.androidrccar.host.logic.handler.RequestCameraMessageHandler;
import to.sven.androidrccar.host.logic.handler.RotateCameraMessageHandler;
import to.sven.androidrccar.host.logic.handler.SetUpCyclicUpdateMessageHandler;
import to.sven.androidrccar.host.logic.handler.TurnCarMessageHandler;
import to.sven.androidrccar.host.service.contract.IHostConfigurationService;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link AuthenticationMessageHandler}.
 * @author sven
 *
 */
@UsesMocks({ IHostConfigurationService.class, IHostLogicListener.class })
public class AuthenticationMessageHandlerTest extends
		AbstractTestCaseForHostMessageHandler<AuthenticationMessageHandler> {
	
	/**
	 * @see GreetingMessage#authSalt
	 */
	private final static String SALT = "deadbeef42codeba5e42deadcode42deafbeef42";
	
	/**
	 * The correct password in test.
	 */
	private final static String CORRECT_PASSWORD = "somePassword";
	
	/**
	 * The correct password hash in test.
	 */
	private final static String CORRECT_PASSWORD_HASH = "4ca1d689a13a83cc73de252d1ff67fbb1daa6e3c";
	
	/**
	 * Mock
	 * @see AndroidMock#createMock
	 */
	private IHostConfigurationService configMock;
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IHostLogicListener listenerMock;
	
	/**
	 * In addition to {@link AbstractTestCaseForHostMessageHandler#setUp()}
	 * is set up also a mock for {@link IHostConfigurationService}.
	 */
	@Override
	protected void setUp() {
		super.setUp();
		configMock = AndroidMock.createMock(IHostConfigurationService.class);
		AndroidMock.expect(hdcMock.getConfiguration())
				   .andReturn(configMock)
				   .anyTimes();
		listenerMock = AndroidMock.createMock(IHostLogicListener.class);
		AndroidMock.expect(hdcMock.getLogicListener())
				   .andReturn(listenerMock)
				   .anyTimes();
		
		logicMock.clearMessageHandlers();
		AndroidMock.expect(configMock.getPassword())
		   		   .andReturn(CORRECT_PASSWORD);
		AndroidMock.expect(logicMock.getSalt())
				   .andReturn(SALT);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void replayFieldMocks() {
		super.replayFieldMocks();
		AndroidMock.replay(configMock, listenerMock);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void verifyFieldMocks() {
		super.verifyFieldMocks();
		AndroidMock.verify(configMock, listenerMock);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication failed.
	 */
	public void testHandleMessage_AuthenticationFailed() {
		// Configure Test
		logicMock.sendMessage(AndroidMock.isA(AuthenticationFailedMessage.class));
		
		// Run Test
		AuthenticationMessageHandler handler = aHandlerForTest();
		// Something incorrect:
		handler.handleMessage(new AuthenticationMessage("deadbeef42codeba5e42deadcode42deafbeef42"));
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Configures what features "supported" for the test.
	 */
	@SuppressWarnings("javadoc")
	private void configureFeatures(boolean shareCamera,
								   boolean canRotateCamera,
								   boolean adjustableSpeed,
								   boolean driveBackward,
								   boolean shareLocation,
								   boolean shareBearing,
								   boolean shareSpeed,
								   boolean batteryPower) {
		CarFeatures carFeatures = new CarFeatures(40, 50, 60, 70,
												  adjustableSpeed, driveBackward, batteryPower);
		AndroidMock.expect(hdcMock.getCarFeatures())
				   .andReturn(carFeatures)
				   .anyTimes();
		
		AndroidMock.expect(configMock.shareCamera())
				   .andReturn(shareCamera);
		AndroidMock.expect(configMock.canRotateCamera())
				   .andReturn(canRotateCamera)
				   .anyTimes();
		AndroidMock.expect(configMock.shareLocation())
				   .andReturn(shareLocation);
		AndroidMock.expect(configMock.shareBearing())
		   		   .andReturn(shareBearing);
		AndroidMock.expect(configMock.shareSpeed())
		   		   .andReturn(shareSpeed);
	}
	
	/**
	 * Runs the common test when authentication is successful.
	 * Does common verify.
	 * Captures the {@link FeatureMessage} that should be send.
	 * @return Captured {@link FeatureMessage}
	 */
	public FeatureMessage commonHandleMessage_AuthenticationSuccessfullRun() {
		listenerMock.connectedEstablished();
		
		Capture<FeatureMessage> msgCapture = new Capture<FeatureMessage>();
		logicMock.sendMessage(AndroidMock.capture(msgCapture));
		// Run Test
		AuthenticationMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new AuthenticationMessage(CORRECT_PASSWORD_HASH));
		
		// Verify Test
		verifyFieldMocks();
		return msgCapture.getValue();
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting no features.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_noFeatures() {
		// Configure Test
		configureFeatures(false, false, false, false,
						  false, false, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(false, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only battery power.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_BatteryPower() {
		// Configure Test
		configureFeatures(false, false, false, false,
						  false, false, false, true);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		logicMock.registerMessageHandler(SetUpCyclicUpdateMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(false, msg.supportAnyLocationDependingFeature());
		assertEquals(true, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only share location.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_ShareLocation() {
		// Configure Test
		configureFeatures(false, false, false, false,
						  true, false, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		logicMock.registerMessageHandler(SetUpCyclicUpdateMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(true, msg.supportAnyLocationDependingFeature());
		assertEquals(true, msg.location);
		assertEquals(false, msg.bearing);
		assertEquals(false, msg.speed);
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only share bearing.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_ShareBearing() {
		// Configure Test
		configureFeatures(false, false, false, false,
						  false, true, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		logicMock.registerMessageHandler(SetUpCyclicUpdateMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(true, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.location);
		assertEquals(true, msg.bearing);
		assertEquals(false, msg.speed);
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only share speed.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_ShareSpeed() {
		// Configure Test
		configureFeatures(false, false, false, false,
						  false, false, true, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		logicMock.registerMessageHandler(SetUpCyclicUpdateMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(true, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.location);
		assertEquals(false, msg.bearing);
		assertEquals(true, msg.speed);
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only share camera.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_camera() {
		// Configure Test
		configureFeatures(true, false, false, false,
						  false, false, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		logicMock.registerMessageHandler(RequestCameraMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(true, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(false, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only
	 * share camera and rotation of it.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_cameraRotation() {
		// Configure Test
		configureFeatures(true, true, false, false,
						  false, false, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		logicMock.registerMessageHandler(RequestCameraMessageHandler.class);
		logicMock.registerMessageHandler(RotateCameraMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(true, msg.camera);
		assertEquals(true, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(false, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only
	 * adjustable speed.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_adjustableSpeed() {
		// Configure Test
		configureFeatures(false, false, true, false,
						  false, false, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(true, msg.adjustableSpeed);
		assertEquals(false, msg.driveBackward);
		assertEquals(false, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.batteryPower);
	}
	
	/**
	 * Tests {@link AuthenticationMessageHandler#handleMessage}:
	 * The authentication is successful and supporting only
	 * drive backward.
	 */
	public void testHandleMessage_AuthenticationSuccessfull_driveBackward() {
		// Configure Test
		configureFeatures(false, false, false, true,
						  false, false, false, false);
		logicMock.registerMessageHandler(TurnCarMessageHandler.class);
		logicMock.registerMessageHandler(AdjustSpeedMessageHandler.class);
		
		// Run Test
		FeatureMessage msg = commonHandleMessage_AuthenticationSuccessfullRun();
		
		// Verify Test
		assertEquals(false, msg.camera);
		assertEquals(false, msg.supportRotateCamera());
		assertEquals(false, msg.adjustableSpeed);
		assertEquals(true, msg.driveBackward);
		assertEquals(false, msg.supportAnyLocationDependingFeature());
		assertEquals(false, msg.batteryPower);
	}
}
