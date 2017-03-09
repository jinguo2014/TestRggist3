package com.example.cyt.testrggist;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 这个类是主页
 */
public class MainActivity extends Activity {
    private EditText pas;
    private EditText us;
    private static final String TAG = "eee";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Getid();
    }
    private void Getid() {
        pas = (EditText) findViewById(R.id.pas);
        us = (EditText) findViewById(R.id.us);
    }
    public void ii(View v)
    {
        String p = pas.getText().toString();
        String u = us.getText().toString();

        OkHttpClient okHttpClient=new OkHttpClient();
        //RequestBody body=new FormBody.Builder().add("phone",u).add("password",p).build();
//        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body=RequestBody.create(JSON,"{phone:"+u+",password:"+p+"}");
        RequestBody body=new FormBody.Builder().add("phone",u).add("password",p).build();
        Request r=new Request.Builder().url("http://app.dnbjcd.com/regaccount.ac?").post(body).build();
        Callback ca=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();
                Log.i(TAG, "onResponse: "+string);
            }
        };
        okHttpClient.newCall(r).enqueue(ca);

    }
}
