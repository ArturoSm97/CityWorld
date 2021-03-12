package com.example.cityworld.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityworld.Models.City;
import com.example.cityworld.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.RealmResults;

public class AdapterCity extends RecyclerView.Adapter<AdapterCity.MyHolder> {
    private Context context;
    private List<City> cities;
    private int layout;
    private OnItemClickListener itemClickListener;
    private OnButtonClickListener buttonClickListener;

    public interface OnItemClickListener{
        void onItemClickListener(City city, int position);
    }

    public interface OnButtonClickListener{
        void onButtonClickListener(City city, int position);
    }

    public AdapterCity(List<City> cities, int layout, OnItemClickListener itemClickListener, OnButtonClickListener buttonClickListener){
        this.cities = cities;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
        this.buttonClickListener = buttonClickListener;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView textName, textDescription, textPuntation;
        private ImageView imageCity;
        private Button delete;

        public MyHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.idNameCity);
            textDescription = (TextView) itemView.findViewById(R.id.idDescription);
            textPuntation = (TextView) itemView.findViewById(R.id.idPuntaje);
            imageCity = (ImageView) itemView.findViewById(R.id.idImageCity);
            delete = (Button) itemView.findViewById(R.id.idDelete);
        }

        public void bind(final City city, OnItemClickListener itemClickListener, OnButtonClickListener buttonClickListener){
            textName.setText(city.getNombre());
            textDescription.setText(city.getDescription());
            textPuntation.setText(String.valueOf(city.getPuntuacion()));
            Picasso.get().load(city.getLink()).fit().into(imageCity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(city, getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListener.onButtonClickListener(city, getAdapterPosition());
                }
            });
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.bind(cities.get(position), itemClickListener, buttonClickListener);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
