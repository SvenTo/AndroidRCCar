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
package to.sven.androidrccar.client.framework;

import com.orangelabs.rcs.service.api.client.media.video.VideoSurfaceView;

import to.sven.androidrccar.client.logic.impl.ClientLogic;
import to.sven.androidrccar.client.service.contract.IClientConfigurationService;
import to.sven.androidrccar.client.service.contract.IVideoClientService;
import to.sven.androidrccar.client.service.impl.ClientConfigurationService;
import to.sven.androidrccar.client.service.impl.VideoClientService;
import to.sven.androidrccar.common.framework.IFactory;
import android.content.Context;

/**
 * This factory provides creators for all classes that {@link ClientLogic} needed to create.
 * @author sven
 *
 */
public interface IClientFactory extends IFactory {
	
	/**
	 * Creates a new {@link VideoClientService}
	 * @param context see {@link ClientConfigurationService#ClientConfigurationService}
	 * @return Created instance as {@link IClientConfigurationService}.
	 * @see ClientConfigurationService#ClientConfigurationService(android.content.Context)
	 */
	IClientConfigurationService createClientConfigurationService(Context context);

	/**
	 * Creates a new {@link VideoClientService}
	 * @param videoView The view where the video should be shown
	 * @return Created instance as {@link IVideoClientService}
	 * @see VideoClientService#VideoClientService
	 */
	IVideoClientService createVideoClientServicer(VideoSurfaceView videoView);
}
