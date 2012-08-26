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

import org.easymock.Capture;

import to.sven.androidrccar.common.communication.model.CameraConnectionParameterMessage;
import to.sven.androidrccar.common.communication.model.RequestCameraMessage;
import to.sven.androidrccar.host.framework.IHostFactory;
import to.sven.androidrccar.host.logic.handler.RequestCameraMessageHandler;
import to.sven.androidrccar.host.service.contract.ICameraStreamingService;
import android.content.Context;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link RequestCameraMessageHandler}.
 * @author sven
 *
 */
@UsesMocks(ICameraStreamingService.class)
public class RequestCameraHandlerTest extends
		AbstractTestCaseForHostMessageHandler<RequestCameraMessageHandler> {
	
	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private ICameraStreamingService cameraStreamingServiceMock;
	
	/**
	 * In addition to {@link AbstractTestCaseForHostMessageHandler#setUp()}
	 * is set up also a mock for {@link ICameraStreamingService}.
	 */
	@Override
	protected void setUp() {
		super.setUp();
		cameraStreamingServiceMock = AndroidMock.createStrictMock(ICameraStreamingService.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void replayFieldMocks() {
		super.replayFieldMocks();
		AndroidMock.replay(cameraStreamingServiceMock);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void verifyFieldMocks() {
		super.verifyFieldMocks();
		AndroidMock.verify(cameraStreamingServiceMock);
	}
	
	/**
	 * Common run and verify for all test.
	 * @param activateCamera Is request is for activation?
	 */
	private void runAndVerifyTest(boolean activateCamera) {
		// Run Test
		RequestCameraMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new RequestCameraMessage(activateCamera));
		
		// Verify Test
		verifyFieldMocks();
	}
	
	/**
	 * Tests {@link RequestCameraMessageHandler#handleMessage}.
	 * Not started and should be started.
	 */
	@UsesMocks(IHostFactory.class)
	public void testHandleMessage_startAndNotStarted() {
		// Configure Test
		IHostFactory factory = AndroidMock.createStrictMock(IHostFactory.class);
		AndroidMock.expect(hdcMock.getFactory())
				   .andReturn(factory)
				   .anyTimes();
		AndroidMock.expect(hdcMock.getCameraStreamingService())
		   		   .andReturn(null)
		   		   .anyTimes();
		AndroidMock.expect(factory.createCameraStreamingService())
				   .andReturn(cameraStreamingServiceMock);
		cameraStreamingServiceMock.initialize(AndroidMock.isA(Context.class));
		AndroidMock.expect(cameraStreamingServiceMock.getPort())
				   .andReturn(4200);
		AndroidMock.expect(cameraStreamingServiceMock.getScheme())
				   .andReturn("rtsp://");
		AndroidMock.expect(cameraStreamingServiceMock.getPath())
		   		   .andReturn("/video");
		Capture<CameraConnectionParameterMessage> msgCapture =
				new Capture<CameraConnectionParameterMessage>();
		logicMock.sendMessage(AndroidMock.capture(msgCapture));
		hdcMock.setCameraStreamingService(cameraStreamingServiceMock);
		
		// Run Test & Verify
		AndroidMock.replay(factory);
		runAndVerifyTest(true);
		AndroidMock.verify(factory);
		CameraConnectionParameterMessage msg = msgCapture.getValue();
		assertEquals(4200, msg.port);
		assertEquals("rtsp://", msg.scheme);
		assertEquals("/video", msg.path);
	}
	
	/**
	 * Tests {@link RequestCameraMessageHandler#handleMessage}:
	 * Started and should be stopped.
	 */
	public void testHandleMessage_stopAndStarted() {
		// Configure Test
		AndroidMock.expect(hdcMock.getCameraStreamingService())
		   		   .andReturn(cameraStreamingServiceMock)
		   		   .anyTimes();
		cameraStreamingServiceMock.close();
		hdcMock.setCameraStreamingService(null);
		
		// Run Test & Verify
		runAndVerifyTest(false);
	}
	
	/**
	 * Tests {@link RequestCameraMessageHandler#handleMessage}:
	 * Not started and should be stopped.
	 */
	public void testHandleMessage_stopAndNotStarted() {
		// Configure Test
		AndroidMock.expect(hdcMock.getCameraStreamingService())
		   		   .andReturn(null)
		   		   .anyTimes();
		hdcMock.setCameraStreamingService(null);

		// Run Test & Verify
		runAndVerifyTest(false);
	}	
	
	/**
	 * Tests {@link RequestCameraMessageHandler#handleMessage}:
	 * Started and should be started.
	 */
	public void testHandleMessage_startAndStarted() {
		// Configure Test
		AndroidMock.expect(hdcMock.getCameraStreamingService())
		   		   .andReturn(cameraStreamingServiceMock)
		   		   .anyTimes();
		logicMock.handleError(AndroidMock.isA(IOException.class));

		// Run Test & Verify
		runAndVerifyTest(true);
	}
}
