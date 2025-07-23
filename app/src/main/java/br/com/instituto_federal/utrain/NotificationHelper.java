package br.com.instituto_federal.utrain;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;



import java.util.Calendar;

public class NotificationHelper {
    private Context context;
    private AlarmManager alarmManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel workoutChannel = new NotificationChannel(
                    "workout_channel", "Lembretes de Treino",
                    android.app.NotificationManager.IMPORTANCE_HIGH);

            NotificationChannel waterChannel = new NotificationChannel(
                    "water_channel", "Lembretes de Água",
                    android.app.NotificationManager.IMPORTANCE_HIGH);

            android.app.NotificationManager manager = context.getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(workoutChannel);
            manager.createNotificationChannel(waterChannel);
        }
    }

    public void scheduleWorkoutReminder() {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification_type", "workout");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Agenda para 9:00 AM todos os dias
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void scheduleWaterReminder() {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("notification_type", "water");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 200, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Agenda a cada 30 minutos
        long interval = 30 * 60 * 1000; // 30 minutos em ms

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + interval, interval, pendingIntent);
    }

    public void showTestNotification() {
        // Criar e disparar notificação de treino
        Intent workoutIntent = new Intent(context, NotificationReceiver.class);
        workoutIntent.putExtra("notification_type", "workout");

        NotificationReceiver receiver = new NotificationReceiver();
        receiver.onReceive(context, workoutIntent);

        // Usar Handler para atrasar a segunda notificação por 3 segundos
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            Intent waterIntent = new Intent(context, NotificationReceiver.class);
            waterIntent.putExtra("notification_type", "water");

            NotificationReceiver receiver2 = new NotificationReceiver();
            receiver2.onReceive(context, waterIntent);
        }, 5000); // 3000ms = 3 segundos
    }
}
