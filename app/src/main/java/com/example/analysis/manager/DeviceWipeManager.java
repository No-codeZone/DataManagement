package com.example.analysis.manager;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

public class DeviceWipeManager extends DeviceAdminReceiver {
    @Override
    public void onEnabled(@NonNull Context context, @NonNull Intent intent) {
        // Silent logging of admin activation
        Log.d("DeviceAdmin", "Device admin enabled silently");
    }

    @Override
    public void onDisabled(@NonNull Context context, @NonNull Intent intent) {
        // Silent logging of admin deactivation
        Log.d("DeviceAdmin", "Device admin disabled silently");
    }
}