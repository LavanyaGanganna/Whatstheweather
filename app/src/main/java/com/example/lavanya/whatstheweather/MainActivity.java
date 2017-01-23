package com.example.lavanya.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    private static final String TAG =MainActivity.class.getSimpleName() ;
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= (EditText) findViewById(R.id.editText);
        textView= (TextView) findViewById(R.id.textView2);

    }

    public void findweather(View view){
        String cityname="";
        InputMethodManager methodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        try {
            cityname=URLEncoder.encode(editText.getText().toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        textView.setText("");
        WeatherTask task=new WeatherTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+ cityname+ "&APPID=55043ca149550f44459ac5108bf6f7ac");

    }
    public class WeatherTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection httpURLConnection;
            String result="";
            try {
                url=new URL(strings[0]);
                httpURLConnection= (HttpURLConnection) url.openConnection();
                InputStream inputStream= httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader= new InputStreamReader(inputStream);
                int data= inputStreamReader.read();
                while(data != -1){
                    char current= (char) data;
                    result=result+ current;
                    data=inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"couldnt find",Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("weather");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.i("main", object.getString("main"));
                        Log.i("description", object.getString("description"));
                        textView.setText(object.getString("main") + " : " + object.getString("description"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }
}
