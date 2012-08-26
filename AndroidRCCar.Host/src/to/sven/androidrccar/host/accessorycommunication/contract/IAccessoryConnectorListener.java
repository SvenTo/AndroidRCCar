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
package to.sven.androidrccar.host.accessorycommunication.contract;

import to.sven.androidrccar.host.accessorycommunication.impl.AccessoryConnector;
import to.sven.androidrccar.host.framework.IHostDependencyContainer;
import android.content.Context;

/**
 * Informs the implementor when something happens in the {@link AccessoryConnector}.
 * 
 * @author sven
 */
public interface IAccessoryConnectorListener {
	
	/**
	 * An accessory was connected and it was successfully opened. 
	 * The {@link IAccessoryCommunication} is set in the {@link IHostDependencyContainer}.
	 */
	void accessoryOpend();
	
	/**
	 * The opened accessory was detached from the phone.
	 */
	void accessoryDetached();
	
	/**
	 * Something went wrong during connecting to the accessory.
	 * @param resId Android String Resource id 
	 * @param args Additional arguments that will be used to format the string resource.
	 * @see Context#getText(int)
	 * @see String#format(String, Object...)
	 */
	void error(int resId, Object... args);
}
