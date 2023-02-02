package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragA.FragAListener{
    private MainViewModel model;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, new MySettingsFragment()).addToBackStack(null).commit();
            default:
               return false;
        }
    }
}