package com.fortmeier.flightsearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fortmeier.flightsearch.api.JsonPlaceHolderApi;
import com.fortmeier.flightsearch.api.requestData.Airport.Airport;
import com.fortmeier.flightsearch.api.requestData.Authentication.Authentication;

import com.fortmeier.flightsearch.api.requestData.Flight.Data;
import com.fortmeier.flightsearch.api.requestData.Flight.FlightsRawData;
import com.fortmeier.flightsearch.api.requestData.Coordinates.Coordinates;
import com.fortmeier.flightsearch.model.AccessKey;
import com.fortmeier.flightsearch.model.Flight;
import com.fortmeier.flightsearch.model.FlightsList;
import com.fortmeier.flightsearch.utility.CustomDatePicker;
import com.fortmeier.flightsearch.viewmodel.SearchInput;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private String accessToken;

    private Button searchButton;
    private Button dayOfDeparture;

    private EditText editTextDeparture;
    private EditText editTextDestination;

    private CheckBox searchingWithIataCodes;
    private CheckBox onlyDirectFlights;

    private CustomDatePicker customDatePicker;

    private SearchInput searchInput;

    private TextView textViewStatus;

    private AccessKey accessKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = new SearchInput();
        accessKey = new AccessKey();
        customDatePicker = new CustomDatePicker();

        assignId(searchButton, R.id.button_search);
        editTextDeparture = findViewById(R.id.edit_text_departure);
        editTextDestination = findViewById(R.id.edit_text_destination);
        textViewStatus = findViewById(R.id.text_view_status);
        dayOfDeparture = findViewById(R.id.material_button_day_of_departure);
        searchingWithIataCodes = findViewById(R.id.checkbox_iatacode_search);
        onlyDirectFlights = findViewById(R.id.checkbox_only_direct_flights);


        searchInput.setDateOfDeparture(Calendar.getInstance().getTime());
        customDatePicker.initDatePicker(dayOfDeparture, this);
        dayOfDeparture.setText(customDatePicker.getTodaysDate());

        /*
         * Logging output for the communication between server and client in Logcat
         */

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        /*
         * Adding logger interceptor.
         * Adding interceptor, that adds a header to all requests.
         * Important for sending the key.
         */

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        /*
         * Executing Get-Request.
         * The interface defines the relative URL. Here the baseURL is passed.
         * AddConverterFactory converts the json-object in java-object.
         */

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://test.api.amadeus.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getAccessTokenRequest();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_search:
                textViewStatus.setTextColor(Color.parseColor("#000000"));
                textViewStatus.setText("Please wait ...");
                searchInput.setDateOfDeparture(customDatePicker.getDate());
                if (searchingWithIataCodes.isChecked()) {
                    getLocationNameCountry(accessToken, editTextDeparture.getText().toString(), true);
                } else {
                    getLocationCoordinatesRequest(accessToken, editTextDeparture.getText().toString(), true);
                }
                break;
        }
    }

    public void assignId(Button btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void getAccessTokenRequest() {
        Map<String, String> fields = new HashMap<>();
        fields.put("grant_type", accessKey.getGrant_type());
        fields.put("client_id", accessKey.getClient_id());
        fields.put("client_secret", accessKey.getClient_secret());


        Call<Authentication> call = jsonPlaceHolderApi.createPostForToken(fields);

        call.enqueue(new Callback<Authentication>() {
            @Override
            public void onResponse(Call<Authentication> call, Response<Authentication> response) {
                if (!response.isSuccessful()) {
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                    textViewStatus.setText("Request failed.");
                    return;
                }
                Authentication postResponse = response.body();

                double[] cooridnates = getLocation();

                if (cooridnates != null && accessToken == null) {
                    getNextAirportRequest(postResponse.getAccess_token(), cooridnates, true, true);
                }

                accessToken = postResponse.getAccess_token();
            }

            @Override
            public void onFailure(Call<Authentication> call, Throwable t) {
                textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                textViewStatus.setText("Error: " + t.getMessage());
            }

        });
    }

    public void getNextAirportRequest(String accessToken, double[] coordinates, boolean onCreate, boolean departure) {
        Call<Airport> call = jsonPlaceHolderApi.getNextAirport("Bearer " + accessToken, coordinates[0], coordinates[1], 500, 1, "distance");

        call.enqueue(new Callback<Airport>() {
            @Override
            public void onResponse(Call<Airport> call, Response<Airport> response) {
                if (!response.isSuccessful()) {
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                    textViewStatus.setText("Request failed. Try again.");
                    return;
                }
                Airport postResponse = response.body();
                if (onCreate) {
                    String startAirportRawData = postResponse.getData().get(0).getAddress().getCityName().toLowerCase(Locale.ROOT);
                    String startAirport = startAirportRawData.substring(0, 1).toUpperCase() + startAirportRawData.substring(1);
                    editTextDeparture.setText(startAirport);
                } else {
                    try {
                        if (departure) {
                            searchInput.setDepartureCityName(postResponse.getData().get(0).getAddress().getCityName());
                            searchInput.setDepartureCityCode(postResponse.getData().get(0).getAddress().getCityCode());
                            searchInput.setDepartureCountryName(postResponse.getData().get(0).getAddress().getCountryName());

                            getNextAirportRequest(accessToken, new double[]{searchInput.getDestinationLatitude(), searchInput.getDestinationLongitude()}, false, false);
                        } else {
                            searchInput.setDestinationCityName(postResponse.getData().get(0).getAddress().getCityName());
                            searchInput.setDestinationCityCode(postResponse.getData().get(0).getAddress().getCityCode());
                            searchInput.setDestinationCountryName(postResponse.getData().get(0).getAddress().getCountryName());

                            getFlightsListRequest(accessToken, searchInput.getDepartureCityCode(), searchInput.getDestinationCityCode(), searchInput.getDateOfDeparture(), onlyDirectFlights.isChecked());
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }

            @Override
            public void onFailure(Call<Airport> call, Throwable t) {
                textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                textViewStatus.setText("Error: " + t.getMessage());
            }
        });
    }

    public void getLocationCoordinatesRequest(String accessToken, String cityName, boolean departure) {
        Call<Coordinates> call = jsonPlaceHolderApi.getLocation("Bearer " + accessToken, "CITY", cityName, 1);

        call.enqueue(new Callback<Coordinates>() {
            @Override
            public void onResponse(Call<Coordinates> call, Response<Coordinates> response) {
                if (!response.isSuccessful()) {
                    getAccessTokenRequest();
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                    textViewStatus.setText("Request failed. Try again.");
                    return;
                }

                Coordinates postResponse = response.body();
                if (postResponse.getData().size() != 0) {
                    if (departure) {
                        searchInput.setDepartureLatitude(postResponse.getData().get(0).getGeocode().getLatitude());
                        searchInput.setDepartureLongitude(postResponse.getData().get(0).getGeocode().getLongitude());
                        getLocationCoordinatesRequest(accessToken, editTextDestination.getText().toString(), false);
                    } else {
                        searchInput.setDestinationLatitude(postResponse.getData().get(0).getGeocode().getLatitude());
                        searchInput.setDestinationLongitude(postResponse.getData().get(0).getGeocode().getLongitude());
                        getNextAirportRequest(accessToken, new double[]{searchInput.getDepartureLatitude(), searchInput.getDepartureLongitude()}, false, true);

                    }
                } else {
                    if (departure) {
                        textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                        textViewStatus.setText("Departure airport not found.");
                    } else {
                        textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                        textViewStatus.setText("Destination airport not found.");
                    }
                }

            }

            @Override
            public void onFailure(Call<Coordinates> call, Throwable t) {
                getAccessTokenRequest();
                textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                textViewStatus.setText("Error: " + t.getMessage());
            }
        });
    }

    public void getLocationNameCountry(String accessToken, String iataCode, boolean departure) {
        Call<Coordinates> call = jsonPlaceHolderApi.getLocation("Bearer " + accessToken, "AIRPORT", iataCode, 1);

        call.enqueue(new Callback<Coordinates>() {
            @Override
            public void onResponse(Call<Coordinates> call, Response<Coordinates> response) {
                if (!response.isSuccessful()) {
                    getAccessTokenRequest();
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                    textViewStatus.setText("Request failed. Try again.");
                    return;
                }

                Coordinates postResponse = response.body();
                if (departure) {
                    searchInput.setDepartureCityName(postResponse.getData().get(0).getAddress().getCityName());
                    searchInput.setDepartureCountryName(postResponse.getData().get(0).getAddress().getCountryName());
                    getLocationNameCountry(accessToken, editTextDestination.getText().toString(), false);
                } else {
                    searchInput.setDestinationCityName(postResponse.getData().get(0).getAddress().getCityName());
                    searchInput.setDestinationCountryName(postResponse.getData().get(0).getAddress().getCountryName());
                    getFlightsListRequest(accessToken, editTextDeparture.getText().toString(), editTextDestination.getText().toString(), searchInput.getDateOfDeparture(), onlyDirectFlights.isChecked());
                }


            }

            @Override
            public void onFailure(Call<Coordinates> call, Throwable t) {
                textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                textViewStatus.setText("Error: " + t.getMessage());
            }
        });
    }


    public void getFlightsListRequest(String accessToken, String originLocationCode, String destinationLocationCode, Date departureDate, boolean nonStop) {


        Call<FlightsRawData> call = jsonPlaceHolderApi.getFlights("Bearer " + accessToken, originLocationCode, destinationLocationCode, new SimpleDateFormat("yyyy-MM-dd").format(departureDate), "1", nonStop, "EUR", 10);

        call.enqueue(new Callback<FlightsRawData>() {
            @Override
            public void onResponse(Call<FlightsRawData> call, Response<FlightsRawData> response) {
                if (!response.isSuccessful()) {
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                    textViewStatus.setText("Request failed. Try again.");
                    return;
                }
                FlightsRawData postResponse = response.body();
                List<Flight> flights = getFormattedFlights(postResponse.getData());

                if (flights.size() == 0) {
                    textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                    textViewStatus.setText("No matching flights found.");
                } else {
                    Intent intent = new Intent(MainActivity.this, FlightsActivity.class);
                    FlightsList flightsList = new FlightsList(flights);
                    intent.putExtra("FLIGHT_LIST", flightsList);
                    textViewStatus.setText("");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<FlightsRawData> call, Throwable t) {
                textViewStatus.setTextColor(Color.parseColor("#FF0000"));
                textViewStatus.setText("Error: " + t.getMessage());
            }
        });
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public double[] getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return retrieveLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return null;
        }
    }

    @SuppressLint("MissingPermission")
    private double[] retrieveLocation() {
        LocationManager manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);


        android.location.Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            double cooridnates[] = new double[2];
            cooridnates[0] = location.getLatitude();
            cooridnates[1] = location.getLongitude();
            return cooridnates;
        } else {
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length != 0) {
            if (requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                retrieveLocation();
            }
        } else {
            System.out.println("Permission Denied");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    public void openDatePicker(View view) {
        customDatePicker.getDatePickerDialog().show();
    }


    public List<Flight> getFormattedFlights(List<Data> dataList) {
        List<Flight> flights = new ArrayList<>();
        int numberOfFlights = dataList.size();


        for (int i = 0; i < numberOfFlights; i++) {
            Data dataset = dataList.get(i);
            int numberOfSegments = dataset.getItineraries().get(0).getSegments().size();
            flights.add(new Flight(
                            searchInput.getDepartureCityName(),
                            searchInput.getDepartureCountryName(),
                            dataset.getItineraries().get(0).getSegments().get(0).getDeparture().getAt().replace("T", "  "),
                            searchInput.getDestinationCityName(),
                            searchInput.getDestinationCountryName(),
                            dataset.getItineraries().get(0).getSegments().get(numberOfSegments - 1).getArrival().getAt().replace("T", "  "),
                            String.valueOf(numberOfSegments - 1),
                            dataset.getItineraries().get(0).getSegments().get(0).getCarrierCode(),
                            dataset.getPrice().getGrandTotal() + "€"
                    )
            );
        }


        return flights;
    }
}
