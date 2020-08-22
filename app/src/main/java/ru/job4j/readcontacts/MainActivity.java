package ru.job4j.readcontacts;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;

public abstract class MainActivity extends FragmentActivity {
    public static final String MAIN_FOR = "main_for";
    public abstract Fragment loadFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_fragment);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FragmentManager manager = getSupportFragmentManager();
        if (manager.findFragmentById(R.id.content) == null) {
            manager.beginTransaction().add(R.id.content, loadFragment()).commit();
        }
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .READ_CONTACTS}, 0);
        }
    }
}
