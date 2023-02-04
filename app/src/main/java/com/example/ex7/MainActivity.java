package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceFragmentCompat;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragA.FragAListener{
    private MainViewModel model;
    private BroadcastReceiver br = new BatteryReceiver();
    static Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragB fragB = (FragB) getSupportFragmentManager().findFragmentByTag("FRAGB");
        FragmentContainerView fragBContainer = (FragmentContainerView)findViewById(R.id.fragContainer);
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)){
            if (fragB != null) {
                getSupportFragmentManager().beginTransaction().show(fragB).commit();
            }

            else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragContainer, FragB.class,null, "FRAGB") .commit();
            }
        }
        getSupportFragmentManager().executePendingTransactions();
        IntentFilter i = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br,i);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                Toast.makeText(getApplicationContext(), "Time Is Over", Toast.LENGTH_LONG).show();
            }
        };
    }

    static void workerThread() {
        // And this is how you call it from the worker thread:
        int command = 1;
        String parameter = "Finished!";
        Message message = mHandler.obtainMessage(command, parameter);
        message.sendToTarget();
    }

    @Override
    public void OnClickEvent() {
        FragB fragB;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragContainer, FragB.class, null,"FRAGB")
                    .addToBackStack("BBB")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        fragB = (FragB) getSupportFragmentManager().findFragmentByTag("FRAGB");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        IntentFilter i = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(br,i);
    }

    public static class MySettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void showAlertDialogForExit() {
        FragmentManager fm = getSupportFragmentManager();
        ExitDialog alertDialog = new ExitDialog();
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, new MySettingsFragment()).addToBackStack(null).commit();
                break;
            case R.id.exit:
                showAlertDialogForExit();
                break;
            default:
                return false;

        }
        return true;
    }
}