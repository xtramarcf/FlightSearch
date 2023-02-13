package com.fortmeier.flightsearch.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortmeier.flightsearch.R;
import com.fortmeier.flightsearch.api.requestData.Flight.FlightsRawData;
import com.fortmeier.flightsearch.model.Flight;

import java.util.ArrayList;
import java.util.List;


// <FlightAdapter.Flightholder>: RecyclerView Adapter knows that the Viewholder we want to use
public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.FlightHolder> {

    private List<Flight> flights = new ArrayList<>();


    /*
     * Creating und returning FlightHolder
     * Layout, for single Items in RecyclerView
     * ViewGroup parent is the recyclerView itself
     */
    @NonNull
    @Override
    public FlightHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight, parent, false);
        return new FlightHolder(itemView);
    }

    /*
    * Getting data from java objects to view holder
     */
    @Override
    public void onBindViewHolder(@NonNull FlightHolder holder, int position) {
        Flight currentFlight = flights.get(position);

        holder.textViewDeparture.setText(currentFlight.getDeparture());
        holder.textViewDepartureCountry.setText(currentFlight.getDepartureCountry());
        holder.textViewDepartureTime.setText(currentFlight.getDepartureTime());
        holder.textViewDestination.setText(currentFlight.getDestination());
        holder.textViewDestinationCountry.setText(currentFlight.getDestinationCountry());
        holder.textViewDestinationTime.setText(currentFlight.getDestinationTime());
        holder.textViewStops.setText("Stopovers: "+currentFlight.getStops());
        holder.textViewAirline.setText("IATA Airline-Code: "+currentFlight.getIataAirlineCode());
        holder.textViewPrice.setText(currentFlight.getPrice());
    }


    /*
    * Returns, how many items will be displayed in recyclerView. All Items should be displayed
     */
    @Override
    public int getItemCount() {
        return flights.size();
    }

    /*
    * Way to get the list of flights in RecyclerView with notifyDataSetChanged
    * Method is not the best solution. 'NotifyDataSetChanged()' should be changed later
     */
    public void setFlights(List<Flight> flights){
        this.flights = flights;
        notifyDataSetChanged();
    }

    class FlightHolder extends RecyclerView.ViewHolder{

        private TextView textViewDeparture;
        private TextView textViewDepartureCountry;
        private TextView textViewDepartureTime;
        private TextView textViewDestination;
        private TextView textViewDestinationCountry;
        private TextView textViewDestinationTime;
        private TextView textViewStops;
        private TextView textViewAirline;
        private TextView textViewPrice;


        public FlightHolder(View itemView) {
            super(itemView);
            textViewDeparture = itemView.findViewById(R.id.text_view_departure);
            textViewDepartureCountry = itemView.findViewById(R.id.text_view_departure_country);
            textViewDepartureTime = itemView.findViewById(R.id.text_view_departure_time);
            textViewDestination = itemView.findViewById(R.id.text_view_destination);
            textViewDestinationCountry = itemView.findViewById(R.id.text_view_destination_country);
            textViewDestinationTime = itemView.findViewById(R.id.text_view_destination_time);
            textViewStops = itemView.findViewById(R.id.text_view_stops);
            textViewAirline = itemView.findViewById(R.id.text_view_airline);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
        }
    }
}
