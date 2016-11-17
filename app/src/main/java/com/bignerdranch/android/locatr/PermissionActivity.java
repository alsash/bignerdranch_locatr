package com.bignerdranch.android.locatr;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class PermissionActivity extends SingleFragmentActivity {
    private static final int PERMISSION_REQUIRED = 1;
    private static final int PERMISSION_OPTIONAL = 2;

    protected abstract String[] requestRequiredPermissions();

    protected void onRequiredPermissionsResult(String[] nonGrantedPermissions) {
        String text = (nonGrantedPermissions.length > 1) ?
                "Required Permissions Not Granted" :
                "Required Permission Not Granted";
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        finish();
    }

    protected String[] requestOptionalPermissions() {
        return null;
    }

    protected void onOptionalPermissionsResult(String[] nonGrantedPermissions) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        grantPermissions(requestRequiredPermissions(), PERMISSION_REQUIRED);
        grantPermissions(requestOptionalPermissions(), PERMISSION_OPTIONAL);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUIRED:
            case PERMISSION_OPTIONAL:
                ArrayList<String> nonGrantedPermissions = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        nonGrantedPermissions.add(permissions[i]);
                    }
                }
                if (nonGrantedPermissions.size() == 0) return;

                String[] permissionsArray = nonGrantedPermissions.toArray(
                        new String[nonGrantedPermissions.size()]);

                if (requestCode == PERMISSION_REQUIRED) {
                    onRequiredPermissionsResult(permissionsArray);
                } else {
                    onOptionalPermissionsResult(permissionsArray);
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void grantPermissions(String[] permissions, int requestCode) {
        if (permissions == null || permissions.length == 0) return;

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
                requestCode
        );
    }
}
