package hu.bme.aut.unicalendar;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hu.bme.aut.unicalendar.data.CalendarDatabase;
import hu.bme.aut.unicalendar.data.Event;

public class MyService extends Service {

    public static boolean running = false;

    private static final String NOTIFICATION_CHANNEL_ID = "time_service_notifications";
    private static final String NOTIFICATION_CHANNEL_NAME = "Time Service notifications";

    private static final int NOTIF_FOREGROUND_ID = 101;

    private boolean enabled = false;

    private MyTimeThread myTimeThread = null;

    private class MyTimeThread extends Thread {
        boolean firstNotification = true;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public void run() {
            Handler h = new Handler(MyService.this.getMainLooper());

            while (enabled) {
                if (firstNotification) {
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyService.this,
                                    "UniCalendar will send notifications every 10 seconds.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    firstNotification = false;
                }
                else {
                    CalendarDatabase database = Room.databaseBuilder(
                            getApplicationContext(),
                            CalendarDatabase.class,
                            "calendar").build();

                    Calendar today = Calendar.getInstance();
                    List<Event> rows = database.eventDao().getAll();

                    int events = 0;

                    for(Event event : rows) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(event.date);
                        long subtract_long = event.notification;
                        int subtract_int = (int)subtract_long;
                        cal.add(Calendar.DAY_OF_MONTH, -subtract_int);
                        if (    today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                                today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
                                today.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))
                            events++;
                    }

                    if (events > 0)
                    {
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyService.this,
                                        "UniCalendar has sent a notification.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent notificationIntent = new Intent(MyService.this, MainActivity.class);

                        PendingIntent contentIntent = PendingIntent.getActivity(
                                MyService.this,
                                0,
                                notificationIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification n  = new Notification.Builder(MyService.this)
                                .setContentTitle("Agony is coming...")
                                .setContentText(events + " uni " + (events > 1 ? "events" : "event") + " in the near future.")
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentIntent(contentIntent)
                                .setVibrate(new long[] { 75, 75, 225, 75, 75, 75, 75, 75, 225, 75, 525, 75, 225, 75 })
                                .setAutoCancel(true).build();

                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        notificationManager.notify(0, n);
                    }
                }

                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        enabled = true;
        if (myTimeThread == null) {
            myTimeThread = new MyTimeThread();
            myTimeThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        running = false;
        stopForeground(true);
        enabled = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
