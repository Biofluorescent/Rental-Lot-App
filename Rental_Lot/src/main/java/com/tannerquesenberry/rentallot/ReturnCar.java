package com.tannerquesenberry.rentallot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReturnCar extends AppCompatActivity {

    private EditText mCarID;
    private Button mReturnCar;

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private JSONObject mJSONObject = new JSONObject();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_car);

        // Set corresponding input locations
        mCarID = (EditText) findViewById(R.id.returnCarID);
        mReturnCar = (Button) findViewById(R.id.returnCar);

        // On pressing return car button
        mReturnCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create request to API
                RequestBody requestBody = RequestBody.create(JSON, mJSONObject.toString());
                Request request = new Request.Builder()
                        .url("https://mobile-final-197403.appspot.com/available/" + mCarID.getText().toString())
                        .patch(requestBody)
                        .build();

                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String r = response.body().string();
                        Log.i("Success: ", r);
                    }
                });

                // Return to start screen
                Intent intent = new Intent(ReturnCar.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
