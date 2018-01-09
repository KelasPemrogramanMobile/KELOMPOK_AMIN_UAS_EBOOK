package com.odilogy.util;

import com.odilogy.ebookstore.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

public class NotificationHelper {
	private Context mContext;
	private int NOTIFICATION_ID = 0;
	private Notification mNotification;
	private NotificationManager mNotificationManager;
	private PendingIntent mContentIntent;
	private CharSequence mContentTitle;
	private Builder mBuilder;
	private String mContentText;

	public NotificationHelper(Context context) {
		mContext = context;
	}
	
	public void setContentText(String text){
		mContentText = text;
	}
	
	/**
	 * Put the notification into the status bar
	 */
	@SuppressWarnings("deprecation")
	public void createNotification(String ticker,int notif_id) {
		NOTIFICATION_ID = notif_id;
		// get the notification manager
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

		// create the notification
		mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setContentTitle("Downloading "+ticker)
		    .setContentText(mContentText)
		    .setSmallIcon(android.R.drawable.stat_sys_download);
		mBuilder.setProgress(100, 0, false);
		// show the notification
		mNotificationManager.notify(notif_id,mBuilder.build());
	}
			
	/**
	 * Receives progress updates from the background task and updates the status
	 * bar notification appropriately
	 * @param percentageComplete
	 */
	@SuppressWarnings("deprecation")
	public void progressUpdate(int percentageComplete) {
		// build up the new status message
		CharSequence contentText = percentageComplete + "% complete";
		// publish it to the status bar
		mNotification.setLatestEventInfo(mContext, mContentTitle, contentText,
				mContentIntent);
		mBuilder.setProgress(100, percentageComplete, false);
		mBuilder.setContentText(mContentText);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
//		mNotificationManager.notify(NOTIFICATION_ID, mNotification);
	}

	/**
	 * called when the background task is complete, this removes the
	 * notification from the status bar. We could also use this to add a new
	 * ‘task complete’ notification
	 */
	public void completed() {
		// remove the notification from the status bar
		mNotificationManager.cancel(NOTIFICATION_ID);
	}
}
