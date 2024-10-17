package com.example.flashtorch;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Switch backFlashlightButton;
    private Switch frontFlashlightButton;
    private Switch bothFlashlightButton;
    private CameraManager cameraManager;
    private String backCameraId, frontCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backFlashlightButton = findViewById(R.id.back);
        frontFlashlightButton = findViewById(R.id.front);
        bothFlashlightButton = findViewById(R.id.both);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // Get camera IDs for front and back cameras
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                // Assumes the back camera is the first one and front is second; adapt as needed.
                if (cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    backCameraId = cameraId;
                } else if (cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    frontCameraId = cameraId;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        // Set up listeners
        backFlashlightButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                turnOnBackFlash();
                frontFlashlightButton.setChecked(false);
                bothFlashlightButton.setChecked(false);
            } else {
                turnOffFlash(backCameraId);
            }
        });

        frontFlashlightButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                turnOnFrontFlash();
                backFlashlightButton.setChecked(false);
                bothFlashlightButton.setChecked(false);
            } else {
                turnOffFlash(frontCameraId);
            }
        });

        bothFlashlightButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                turnOnBackFlash();
                turnOnFrontFlash();
                backFlashlightButton.setChecked(false);
                frontFlashlightButton.setChecked(false);
            } else {
                turnOffFlash(backCameraId);
                turnOffFlash(frontCameraId);
            }
        });
    }

    private void turnOnBackFlash() {
        if (backCameraId != null) {
            try {
                cameraManager.setTorchMode(backCameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOnFrontFlash() {
        if (frontCameraId != null) {
            try {
                cameraManager.setTorchMode(frontCameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void turnOffFlash(String cameraId) {
        if (cameraId != null) {
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
