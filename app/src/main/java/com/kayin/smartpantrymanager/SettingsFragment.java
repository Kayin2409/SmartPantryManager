package com.kayin.smartpantrymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

@SuppressWarnings("unused")
public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "SmartPantryPrefs";
    private static final String KEY_EXPIRING_ALERTS = "expiring_alerts_enabled";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SwitchMaterial switchAlerts = view.findViewById(R.id.switch_expiring_alerts);

        // Load saved preference
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean alertsEnabled = prefs.getBoolean(KEY_EXPIRING_ALERTS, false);
        switchAlerts.setChecked(alertsEnabled);

        // Handle toggle changes
        switchAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save preference
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_EXPIRING_ALERTS, isChecked);
            editor.apply();

            String msg = isChecked ? "Expiring alerts enabled" : "Expiring alerts disabled";
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }
}
