package com.tannerquesenberry.rentallot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyCar extends AppCompatActivity {

    private EditText mCarID;
    private EditText mMake;
    private EditText mModel;
    private EditText mYear;
    private EditText mColor;
    private Button mModifyCar;

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private JSONObject mJSONObject = new JSONObject();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_car);

        // Set corresponding input locations
        mCarID = (EditText) findViewById(R.id.modifyCarID);
        mMake = (EditText) findViewById(R.id.modifyMake);
        mModel = (EditText) findViewById(R.id.modifyModel);
        mYear = (EditText) findViewById(R.id.modifyYear);
        mColor = (EditText) findViewById(R.id.modifyColor);
        mModifyCar = (Button) findViewById(R.id.modifyCar);


        // On pressing create car button
        mModifyCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get info
                try {
                    mJSONObject.put("make", mMake.getText().toString());
                    mJSONObject.put("model", mModel.getText().toString());
                    String number = mYear.getText().toString();
                    mJSONObject.put("year", Integer.parseInt(number));
                    mJSONObject.put("color", mColor.getText().toString());
                } catch (JSONException e){
                    e.printStackTrace();
                }

                // Create request to API
                RequestBody requestBody = RequestBody.create(JSON, mJSONObject.toString());
                Request request = new Request.Builder()
                        .url("https://mobile-final-197403.appspot.com/car/" + mCarID.getText().toString())
                        .put(requestBody)
                        .build();

                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String r = response.body().string();
                        Log.i("Modified", r);
                    }
                });

                // Return to start screen
                Intent intent = new Intent(ModifyCar.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
