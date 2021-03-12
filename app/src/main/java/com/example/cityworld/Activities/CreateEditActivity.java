package com.example.cityworld.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityworld.Models.City;
import com.example.cityworld.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import io.realm.Realm;

public class CreateEditActivity extends AppCompatActivity {
    private ImageView imagenPreview;
    private EditText editNombre, editDescripcion, editLink;
    private RatingBar ratingBar;
    private ImageButton imagenBoton;
    private FloatingActionButton guardar;
    private Realm realm;
    private int cityId;
    private boolean isCreation;
    private City city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit);

        realm = Realm.getDefaultInstance();
        bindUIReference();

        if(getIntent().getExtras() != null){
            cityId = getIntent().getExtras().getInt("id");
            isCreation = false;
        } else
            isCreation = true;

        setActivityTitle();

        if(!isCreation){
            city = getCityById(cityId);
            bindDataToFields();
        }

        imagenBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = editLink.getText().toString();
                if( link.length() > 0)
                    loadImageLinkForPreview(link);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEditNewCity();
            }
        });
    }

    public void bindUIReference(){
        imagenPreview = (ImageView) findViewById(R.id.idImagenPreview);
        editNombre = (EditText) findViewById(R.id.idEditName);
        editDescripcion = (EditText) findViewById(R.id.idEditDescription);
        editLink = (EditText) findViewById(R.id.idEditLink);
        ratingBar = (RatingBar) findViewById(R.id.idRatingBar);
        imagenBoton = (ImageButton) findViewById(R.id.idRender);
        guardar = (FloatingActionButton) findViewById(R.id.idSave);
    }

    public void bindDataToFields(){
        editNombre.setText(city.getNombre());
        editDescripcion.setText(city.getDescription());
        editLink.setText(city.getLink());
        ratingBar.setRating(city.getPuntuacion());
        loadImageLinkForPreview(city.getLink());
    }

    public void setActivityTitle() {
        String title = "Edit City";
        if (isCreation) title = "Create New City";
        setTitle(title);
    }

    public City getCityById(int cityId){
        return realm.where(City.class).equalTo("id", cityId).findFirst();
    }

    private boolean isValidDataForNewCity(){
        if(editNombre.getText().toString().length() > 0 && editDescripcion.getText().toString().length() > 0 && editLink.getText().toString().length() >0)
            return true;
        return false;
    }

    public void loadImageLinkForPreview(String link){
        Picasso.get().load(link).fit().into(imagenPreview);
    }

    private void goToMainActivity(){
        Intent intent = new Intent(CreateEditActivity.this , CityActivity.class);
        startActivity(intent);
    }

    private void addEditNewCity() {
        if(isValidDataForNewCity()) {
            String name = editNombre.getText().toString();
            String description = editDescripcion.getText().toString();
            String link = editLink.getText().toString();
            float starts = ratingBar.getRating();

            City city = new City(name, description, link, starts);

            if(!isCreation) city.setId(cityId);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(city);
            realm.commitTransaction();

            goToMainActivity();
        } else
            Toast.makeText(this, "The data is not valid, please check the fields again", Toast.LENGTH_LONG).show();
    }
}
