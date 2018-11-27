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

public class ModifyPatron extends AppCompatActivity {

    private EditText mPatronID;
    private EditText mFName;
    private EditText mLName;
    private EditText mAge;
    private Button mModifyPatron;

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private JSONObject mJSONObject = new JSONObject();
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_patron);

        // Set corresponding input locations
        mPatronID = (EditText) findViewById(R.id.modifyPatronID);
        mFName = (EditText) findViewById(R.id.modifyFName);
        mLName = (EditText) findViewById(R.id.modifyLName);
        mAge = (EditText) findViewById(R.id.modifyAge);
        mModifyPatron = (Button) findViewById(R.id.modifyPatron);

        // On pressing modify patron button
        mModifyPatron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get info
                try {
                    mJSONObject.put("fname", mFName.getText().toString());
                    mJSONObject.put("lname", mLName.getText().toString());
                    String number = mAge.getText().toString();
                    mJSONObject.put("age", Integer.parseInt(number));
                } catch (JSONException e){
                    e.printStackTrace();
                }

                // Create request to API
                RequestBody requestBody = RequestBody.create(JSON, mJSONObject.toString());
                Request request = new Request.Builder()
                        .url("https://mobile-final-197403.appspot.com/patron/" + mPatronID.getText().toString())
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
                Intent intent = new Intent(ModifyPatron.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
