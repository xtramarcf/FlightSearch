package com.fortmeier.flightsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.fortmeier.flightsearch.model.FlightsList;
import com.fortmeier.flightsearch.viewmodel.FlightAdapter;
import com.google.android.material.button.MaterialButton;

public class FlightsActivity extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);

        // Set RecyclerView to layout
        RecyclerView recyclerView = findViewById(R.id.reycler_view);

        // Every recyclerView needs a layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        FlightAdapter adapter = new FlightAdapter();
        // By default, list in adapter is empty. Data needs to be passed with 'onChange' method
        recyclerView.setAdapter(adapter);

        FlightsList flightsList = (FlightsList) getIntent().getSerializableExtra("FLIGHT_LIST");

        adapter.setFlights(flightsList.getFlights());
    }

    @Override
    public void onClick(View view) {

    }

    public void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }
}
