package org.stepic.droid.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.stepic.droid.analytic.Analytic;
import org.stepic.droid.base.App;
import org.stepic.droid.notifications.LocalReminder;
import org.stepic.droid.notifications.StepikNotificationManager;
import org.stepic.droid.preferences.SharedPreferenceHelper;

import javax.inject.Inject;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Inject
    LocalReminder localReminder;

    @Inject
    SharedPreferenceHelper sharedPreferences;

    @Inject
    Analytic analytic;

    @Inject
    StepikNotificationManager stepikNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        App.component().inject(this);
        analytic.reportEvent(Analytic.System.BOOT_COMPLETED);
        long timestamp = sharedPreferences.getNewUserRemindTimestamp();
        if (timestamp > 0L) {
            localReminder.remindAboutApp(timestamp); //send to remind checker
        }
        localReminder.userChangeStateOfNotification();

        stepikNotificationManager.showAllRescheduledNotifications();
    }
}
