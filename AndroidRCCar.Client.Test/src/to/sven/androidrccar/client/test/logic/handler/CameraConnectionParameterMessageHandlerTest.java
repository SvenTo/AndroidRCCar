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

import to.sven.androidrccar.client.logic.handler.CameraConnectionParameterMessageHandler;
import to.sven.androidrccar.common.model.ConnectionParameter;
import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.common.communication.model.CameraConnectionParameterMessage;

import com.google.android.testing.mocking.AndroidMock;
import com.google.android.testing.mocking.UsesMocks;

/**
 * This class should test the behavior of the {@link CameraConnectionParameterMessageHandler}.
 * @author sven
 *
 */
@UsesMocks(IVideoClientService.class)
public class CameraConnectionParameterMessageHandlerTest extends
		AbstractTestCaseForClientMessageHandler<CameraConnectionParameterMessageHandler> {

	/**
	 * Strict mock
	 * @see AndroidMock#createStrictMock
	 */
	private IVideoClientService videoClientMock;
	
	/**
	 * In addition to {@link AbstractTestCaseForClientMessageHandler#setUp()}
	 * is set up also a mock for {@link IVideoClientService}.
	 */
	@Override
	protected void setUp() {
		super.setUp();
		videoClientMock = AndroidMock.createStrictMock(IVideoClientService.class);
		AndroidMock.expect(cdcMock.getVideoClientService())
				   .andReturn(videoClientMock)
				   .anyTimes();
	}
	
	/**
	 * Tests {@link CameraConnectionParameterMessageHandler#handleMessage}:
	 * Checks if the correct URL is passed to the {@link IVideoClientService}.
	 */
	public void testHandleMessage_Success() {
		// Configure Test
		AndroidMock.expect(cdcMock.getConnectionParameter())
				   .andReturn(new ConnectionParameter("42.0.0.1", 4242, null));
		videoClientMock.playStream("rtsp://42.0.0.1:4200/someVideo.mp4");
		
		// Run Test
		AndroidMock.replay(videoClientMock);
		CameraConnectionParameterMessageHandler handler = aHandlerForTest();
		handler.handleMessage(new CameraConnectionParameterMessage(4200, "rtsp", "someVideo.mp4"));
		
		// Verify Test
		verifyFieldMocks();
		AndroidMock.verify(videoClientMock);
	}
	
}
