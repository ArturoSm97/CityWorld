package com.example.cityworld.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.UiAutomation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.cityworld.Adapters.AdapterCity;
import com.example.cityworld.Models.City;
import com.example.cityworld.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class CityActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<City>> {
    private RecyclerView recyclerView;
    private RealmResults<City> cities;
    private AdapterCity cityAdapter;
    private RecyclerView.LayoutManager cityManager;
    private FloatingActionButton add;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.icon_foreground);

        //Realm
        realm = Realm.getDefaultInstance();
        cities = realm.where(City.class).findAll();
        cities.addChangeListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.idListCity);
        add = (FloatingActionButton) findViewById(R.id.idAdd);

        setHideShowFAB();

        cityAdapter = new AdapterCity(cities, R.layout.item, new AdapterCity.OnItemClickListener() {
            @Override
            public void onItemClickListener(City city, int position) {
                Intent intent = new Intent(CityActivity.this, CreateEditActivity.class);
                intent.putExtra("id", city.getId());
                startActivity(intent);
            }
        }, new AdapterCity.OnButtonClickListener() {
            @Override
            public void onButtonClickListener(City city, int position) {
                showAlertForRemovingCity("Delete city", "Are you sure you want to delete " + city.getNombre() + "?", position);
            }
        });
        cityManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(cityAdapter);
        recyclerView.setLayoutManager(cityManager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityActivity.this, CreateEditActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void deleteCity(int position){
        realm.beginTransaction();
        cities.get(position).deleteFromRealm();
        realm.commitTransaction();
    }

    private void setHideShowFAB(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                    add.hide();
                else if (dy < 0)
                    add.show();
            }
        });
    }

    private void showAlertForRemovingCity(String title, String message, final int position){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCity(position);
                        Toast.makeText(CityActivity.this, "It has deleting successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public void onChange(RealmResults<City> cities) {
        cityAdapter.notifyDataSetChanged();
    }
}