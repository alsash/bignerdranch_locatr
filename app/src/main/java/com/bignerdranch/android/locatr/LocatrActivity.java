package com.bignerdranch.android.locatr;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class LocatrActivity extends PermissionActivity {

    @Override
    protected Fragment createFragment() {
        return LocatrFragment.newInstance();
    }

    @Override
    protected String[] requestRequiredPermissions() {
        return new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
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
}
