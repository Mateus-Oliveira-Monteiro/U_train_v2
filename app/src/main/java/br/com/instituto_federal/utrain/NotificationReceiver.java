package br.com.instituto_federal.utrain;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra("notification_type");

        if ("workout".equals(type)) {
            showWorkoutNotification(context);
        } else if ("water".equals(type)) {
            showWaterNotification(context);
        }
    }

    private void showWorkoutNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (!hasNotificationPermission(context) || !notificationManager.areNotificationsEnabled()) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "workout_channel")
                .setSmallIcon(R.drawable.utrainlogo)
                .setContentTitle("Hora do Treino!")
                .setContentText("Não esqueça de treinar hoje 💪")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);

        try {
            notificationManager.notify(1, builder.build());
        } catch (SecurityException e) {
            // Falha silenciosa se não houver permissão
        }
    }

    private void showWaterNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (!hasNotificationPermission(context) || !notificationManager.areNotificationsEnabled()) {
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "water_channel")
                .setSmallIcon(R.drawable.utrainlogo)
                .setContentTitle("Hidrate-se!")
                .setContentText("Hora de beber água 💧")
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Mudança aqui
                .setCategory(NotificationCompat.CATEGORY_REMINDER) // Categoria para lembrete
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Som, vibração, luz
                .setAutoCancel(true);

        try {
            notificationManager.notify(2, builder.build());
        } catch (SecurityException e) {
            // Falha silenciosa se não houver permissão
        }
    }

    private boolean hasNotificationPermission(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
}