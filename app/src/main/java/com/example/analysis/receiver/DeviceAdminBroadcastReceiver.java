package com.example.analysis.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.analysis.manager.DeviceWipeManager;

public class DeviceAdminBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("DeviceAdminReceiver", "Device boot completed. Attempting to activate device admin.");
            activateDeviceAdmin(context);
        }
    }

    private void activateDeviceAdmin(Context context) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager)
                context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminReceiver = new ComponentName(context, DeviceWipeManager.class);

        if (!devicePolicyManager.isAdminActive(deviceAdminReceiver)) {
            try {
                Intent adminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                adminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminReceiver);
                adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(adminIntent);
            } catch (Exception e) {
                Log.e("DeviceAdminReceiver", "Silent activation attempt failed", e);
            }
        }
    }
}