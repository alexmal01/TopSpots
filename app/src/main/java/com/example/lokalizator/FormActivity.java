package com.example.lokalizator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class FormActivity extends AppCompatActivity {
    Button switchToMaps;
    TextView googleLinkText;
    TextView CoordWarning;
    TextView infoText;
    EditText xInput;
    EditText yInput;
    ArrayList<LocationClass> locationsFiltered;
    EditText markerTitleInput;
    EditText descriptionInput;
    RadioGroup typeRadio;
    Button confirmCoords;
    LocationClass location;
    ArrayList<LocationClass> locationsForm;
    XYpoint onClickLocation;
    double xCoordVal;
    double yCoordVal;
    String googleLinkStr;
    String markerTitle;
    TypMiejsca type;
    String description = "opis";
    float rating;
    RatingBar ratingBar;
    RadioButton plener;
    RadioButton bar;
    RadioButton restauracja;
    RadioButton inne;
    boolean isEditiong,plenerValue,barValue,restauracjaValue,inneValue;
    int ratingValue;
    LocationClass locationOld;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        switchToMaps = findViewById(R.id.buttonOpenMaps);
        switchToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivityToMap();
                finish();
            }
        });

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        locationsForm = (ArrayList<LocationClass>) i.getSerializableExtra("lokalizacjeIntent");
        locationsFiltered = (ArrayList<LocationClass>) i.getSerializableExtra("filteredIntent");
        onClickLocation= (XYpoint) i.getSerializableExtra("pojedynczaLokalizacjaIntent");
        isEditiong = (boolean) i.getSerializableExtra("editingIntent");
        ratingValue = 0;
        plenerValue = true;
        barValue = true;
        restauracjaValue = true;
        inneValue = true;
        if(i.getSerializableExtra("rating") != null)
            ratingValue = (Integer) i.getSerializableExtra("rating");
        if(i.getSerializableExtra("plener") != null)
            plenerValue = (boolean) i.getSerializableExtra("plener");
        if(i.getSerializableExtra("bar") != null)
            barValue = (boolean) i.getSerializableExtra("bar");
        if(i.getSerializableExtra("restauracja") != null)
            restauracjaValue = (boolean) i.getSerializableExtra("restauracja");
        if(i.getSerializableExtra("inne") != null)
            inneValue = (boolean) i.getSerializableExtra("inne");

        infoText = findViewById(R.id.giveCoords);
        confirmCoords = findViewById(R.id.confirmCoords);
        googleLinkText = findViewById(R.id.googleTextView);
        CoordWarning = findViewById(R.id.coordOkOrNo);
        xInput = findViewById(R.id.xCoordInput);
        yInput = findViewById(R.id.yCoordInput);
        confirmCoords = findViewById(R.id.confirmCoords);
        markerTitleInput = findViewById(R.id.titleInput);
        typeRadio = findViewById(R.id.typeRadio);
        ratingBar = findViewById(R.id.ratingBar);
        descriptionInput = findViewById(R.id.additionalText);

        plener = findViewById(R.id.radioPlener);
        bar = findViewById(R.id.radioBar);
        restauracja = findViewById(R.id.radioRestauracja);
        inne = findViewById(R.id.radioInne);

//        int radioNr = typeRadio.getCheckedRadioButtonId();
//        RadioButton radioButton = findViewById(radioNr)



        if(onClickLocation != null){
            xInput.setText(((Double) onClickLocation.getX()).toString());
            yInput.setText(((Double) onClickLocation.getY()).toString());
        }

        if(onClickLocation != null){
            xCoordVal = onClickLocation.getX();
            yCoordVal = onClickLocation.getY();
        }

        if(isEditiong){
            infoText.setText("Edytuj marker");
            for(LocationClass lc : locationsForm){//musi znalezc bo kliknalem w niego....
                if(lc.getxCoordinate() == xCoordVal && lc.getyCoordinate() == yCoordVal){
                    location = lc;
                    locationOld = lc;
                    break;
                }
            }
            markerTitleInput.setText(location.getTitle());
            ratingBar.setRating(location.getRating());
            descriptionInput.setText(location.getDescription());
            switch(location.getType()){
                case "PLENER":
                    plener.setChecked(true);
                    break;
                case "BAR":
                    bar.setChecked(true);
                    break;
                case "RESTAURACJA":
                    restauracja.setChecked(true);
                    break;
                case "INNE":
                    inne.setChecked(true);
                    break;
            }



        } else{
            infoText.setText("Nowy marker");
        }

        confirmCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerTitle = markerTitleInput.getText().toString();
                xCoordVal = Double.parseDouble(xInput.getText().toString());
                yCoordVal = Double.parseDouble(yInput.getText().toString());
                googleLinkStr = "https://www.google.com/maps/search/?api=1&query="+ xCoordVal + "," + yCoordVal;
                googleLinkText.setText(googleLinkStr);
                rating = ratingBar.getRating();
                description = descriptionInput.getText().toString();

                if(plener.isChecked()){type = TypMiejsca.PLENER;}
                else if(bar.isChecked()){type = TypMiejsca.BAR;}
                else if(restauracja.isChecked()){type = TypMiejsca.RESTAURACJA;}
                else {type = TypMiejsca.INNE;}

                if (xCoordVal < 52.15 || xCoordVal > 52.35 || yCoordVal < 20.85 || yCoordVal > 21.15){
                    Toast.makeText(FormActivity.this,"Współrzędne poza granicą Warszawy!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //usuwam stary
                    if(isEditiong) {
                        for (LocationClass lc : locationsForm) {
                            if (lc.getxCoordinate() == locationOld.getxCoordinate() && lc.getyCoordinate() == locationOld.getyCoordinate()) {
                                locationsForm.remove(locationOld);
                                Toast.makeText(FormActivity.this,"Zmieniono marker.", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(FormActivity.this,"Dodano marker.", Toast.LENGTH_SHORT).show();
                    }
                    location = new LocationClass(xCoordVal, yCoordVal,markerTitle,type,description,rating);
                    locationsForm.add(location);
                }
            }
        });
    }

    private void switchActivityToMap() {
        System.out.println(ratingValue);
        System.out.println(plenerValue);
        System.out.println(barValue);
        System.out.println(restauracjaValue);
        System.out.println(inneValue);
        boolean conditionFullfilled = false;
        if (plenerValue) {
            conditionFullfilled = true;
            locationsFiltered = (ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("PLENER")).collect(Collectors.toList());
        }
        if (barValue) {
            if(conditionFullfilled)
                locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("BAR")).collect(Collectors.toList()));
            else {
                locationsFiltered = (ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("BAR")).collect(Collectors.toList());
                conditionFullfilled = true;
            }
        }
        if (restauracjaValue) {
            if(conditionFullfilled)
                locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("RESTAURACJA")).collect(Collectors.toList()));
            else {
                locationsFiltered = (ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("RESTAURACJA")).collect(Collectors.toList());
                conditionFullfilled = true;
            }
        }
        if (inneValue) {
            if(conditionFullfilled)
                locationsFiltered.addAll((ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("INNE")).collect(Collectors.toList()));
            else{
                locationsFiltered = (ArrayList<LocationClass>) locationsForm.stream().filter(i -> i.rating >= ratingValue).filter(i -> i.type.equals("INNE")).collect(Collectors.toList());
                conditionFullfilled = true;
            }
        }
        Intent switchActivitiesIntent = new Intent(this, MapsActivity.class);
        switchActivitiesIntent.putExtra("lokalizacjeIntent", locationsForm);
        switchActivitiesIntent.putExtra("filteredIntent",locationsFiltered);
        switchActivitiesIntent.putExtra("rating",ratingValue);
        switchActivitiesIntent.putExtra("plener",plenerValue);
        switchActivitiesIntent.putExtra("bar",barValue);
        switchActivitiesIntent.putExtra("restauracja",restauracjaValue);
        switchActivitiesIntent.putExtra("inne",inneValue);
        startActivity(switchActivitiesIntent);
    }
}