package com.bignerdranch.android.locatr;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;

public class LocatrActivity extends SingleFragmentActivity {
    private static final int PERMISSION_REQUEST_LOCATION = 1;

    @Override
    protected Fragment createFragment() {
        return LocatrFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        grantLocationPermission(new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int returnCode = api.isGooglePlayServicesAvailable(this);
        if (returnCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = api.getErrorDialog(this, returnCode, 0,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    });
            errorDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION:
                boolean allGranted = false;
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        allGranted = true;
                    } else {
                        allGranted = false;
                        break;
                    }
                }
                if (!allGranted) {
                    Toast.makeText(this, "Location permission not granted",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void grantLocationPermission(String[] permissions) {
        ArrayList<String> requiredPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permission);
            }
        }
        if (requiredPermissions.size() == 0) return;

        ActivityCompat.requestPermissions(this,
                requiredPermissions.toArray(new String[requiredPermissions.size()]),
                PERMISSION_REQUEST_LOCATION
        );
    }
}
