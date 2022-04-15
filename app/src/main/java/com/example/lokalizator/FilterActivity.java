package com.example.lokalizator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FilterActivity extends AppCompatActivity {
    double xCoordVal;
    double yCoordVal;
    Button switchToMaps, confirmFilter;
    CheckBox plener,bar,restauracja,inne;
    SeekBar rating;
    ArrayList<LocationClass> locationsForm;
    ArrayList<LocationClass> locationsFiltered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
        switchToMaps = findViewById(R.id.buttonOpenMaps2);
        switchToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivityToMap();
                finish();
            }
        });
        rating = (SeekBar) findViewById(R.id.seekBar);
        rating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangedValue = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(FilterActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
            }
        });
        plener = (CheckBox) findViewById(R.id.checkBox4);
        bar = (CheckBox) findViewById(R.id.checkBox);
        restauracja = (CheckBox) findViewById(R.id.checkBox2);
        inne = (CheckBox) findViewById(R.id.checkBox3);
        confirmFilter = (Button) findViewById(R.id.confirmationButton);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        locationsForm = (ArrayList<LocationClass>) i.getSerializableExtra("lokalizacjeIntent");
        locationsFiltered = new ArrayList<>();
        assert locationsForm != null;
        confirmFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (plener.isChecked())
                    locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= rating.getProgress()).filter(i -> i.type.equals("PLENER")).collect(Collectors.toList()));
                if (bar.isChecked())
                    locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= rating.getProgress()).filter(i -> i.type.equals("BAR")).collect(Collectors.toList()));
                if (restauracja.isChecked())
                    locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= rating.getProgress()).filter(i -> i.type.equals("RESTAURACJA")).collect(Collectors.toList()));
                if (inne.isChecked())
                    locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= rating.getProgress()).filter(i -> i.type.equals("INNE")).collect(Collectors.toList()));

            }
        });
    }
    private void switchActivityToMap() {
        Intent switchActivitiesIntent = new Intent(this, MapsActivity.class);
        switchActivitiesIntent.putExtra("lokalizacjeIntent", locationsForm);
        switchActivitiesIntent.putExtra("filteredIntent",locationsFiltered);
        switchActivitiesIntent.putExtra("rating",rating.getProgress());
        switchActivitiesIntent.putExtra("plener",plener.isChecked());
        switchActivitiesIntent.putExtra("bar",bar.isChecked());
        switchActivitiesIntent.putExtra("restauracja",restauracja.isChecked());
        switchActivitiesIntent.putExtra("inne",inne.isChecked());
        startActivity(switchActivitiesIntent);
    }
}
