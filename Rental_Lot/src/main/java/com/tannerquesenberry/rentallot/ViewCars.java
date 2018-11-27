package com.tannerquesenberry.rentallot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewCars extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cars);

        populateCarList();
    }


    private void populateCarList(){

        // Construct data source
        ArrayList<Car> arrayOfCars = new ArrayList<Car>();

        // Create adapter to convert array to view
        final CarsAdapter adapter = new CarsAdapter(this, arrayOfCars);

        // Attach adapter to listview
        ListView listView = (ListView) findViewById(R.id.carList);
        listView.setAdapter(adapter);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://mobile-final-197403.appspot.com/car")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call,  final Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code " + response);
                }

                    final String responseData = response.body().string();

                    ViewCars.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{

                                JSONArray carsJson = new JSONArray(responseData);
                                ArrayList<Car> newCars = Car.fromJSON(carsJson);
                                adapter.addAll(newCars);

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });


            }
        });


    }


}
