package com.trulloy.updatethroughapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Covid19Data worldData;
    TextView worldTxtView;
    public final static String API_URL_FOR_WORLD_DATA = "http://trulloy.com/covid19api/world.json";

    private class JsonWorldTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            // TODO: Show progress bar
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)
                }

                JSONObject worldobj = new JSONObject(buffer.toString());
                JSONObject data = (JSONObject) worldobj.get(JsonConstant.WORLD);
                worldData = new Covid19Data(JsonConstant.WORLD, 0, data.getInt(JsonConstant.CONFIRMED), data.getInt(JsonConstant.RECOVERED), data.getInt(JsonConstant.DECEASED));
                return "success";
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // TODO: Change the UI and update all state list
            if (worldData != null) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        worldTxtView = findViewById(R.id.worldTxtView);

        new JsonWorldTask().execute(API_URL_FOR_WORLD_DATA);
    }
}
