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
package to.sven.androidrccar.host.controller;

import to.sven.androidrccar.host.R;
import to.sven.androidrccar.host.presentation.UnconnectedActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Shows an {@link Notification} in the Notification-Bar.
 * 
 * @author sven
 *
 */
public class HostServiceNotification {
	
    /**
     * @see NotificationManager
     */
    private NotificationManager notificationManager;
    
    /**
     * The {@link Notification}
     */
    private Notification notification;

    /**
     * Id of the {@link Notification}
     */
	private static int NOTIFICATION_ID = R.string.host_service_notification_id;
	
	/**
	 * Context of the service 
	 */
	private final Context context;

	/**
	 * Default Constructor
	 * @param context Context of the service 
	 */
	@SuppressWarnings("deprecation") // NotificationBuilder is not compatible with API level 10
	public HostServiceNotification(Context context) {
		this.context = context;

    	notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		notification = new Notification(R.drawable.ic_stat_notify, context.getText(R.string.notification_ticker), System.currentTimeMillis());        
        notification.flags = Notification.FLAG_ONGOING_EVENT;
	}
	
	/**
	 * Sets different notification content text depending on the new state.
	 * If the state is {@link ServiceState#NOT_CONNECTED}, the notification will be removed.
	 * @param state The new state
	 */
	public void stateChanged(ServiceState state) {
		switch(state) {
			case WAIT_FOR_OTHER_PHONE:
				changeNotification(R.string.wait_for_other_device);
				break;
			case CONNECTED:
				changeNotification(R.string.connected);
				break;
			case CONNECTION_LOST:
				changeNotification(R.string.connection_lost);
				break;
			case ERROR:
				changeNotification(R.string.error_for_notification);
				break;
			case NOT_CONNECTED:
				cancel();
				break;
		}
	}
	
	/**
	 * Sets the Notification with the given string id.
	 * @param notificationStrId The string id.
	 */
	@SuppressWarnings("deprecation") // NotificationBuilder is not compatible with API level 10
	public void changeNotification(int notificationStrId) {
        // The PendingIntent to launch our activity if the user selects this notification
		Intent intent = new Intent(context, UnconnectedActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(context, context.getText(R.string.app_name),
        							    context.getText(notificationStrId), contentIntent);
        
        // Send the notification.
        notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	/**
	 * Remove the notification.
	 */
	public void cancel() {
		notificationManager.cancel(NOTIFICATION_ID);
	}
}
