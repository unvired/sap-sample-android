package com.unvired.sample.sap.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.unvired.fcm.UnviredFCMMessagingService;
import com.unvired.sync.in.NotificationBO;

/**
 * Follow FCM integration guide and extend UnviredFCMMessagingService.
 * Override onCallBack() to get Notification object from Unvired Library
 */

public class FCMReceiver extends UnviredFCMMessagingService {
	
	@Override
	public void onCallBack(final NotificationBO notificationBO) {
		final String text = notificationBO.getTitle() + " : " + notificationBO.getAlert();
		
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			}
		});
	}
}