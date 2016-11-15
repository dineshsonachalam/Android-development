package com.example.dinesh.fullcontact;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    TextView responseView;
    ImageView photoView;

    static final String NASA_API_URL = "https://api.nasa.gov/planetary/apod?api_key=ikEFNaSgGvRU2gvpRjudv0gzPwaO48ewELP1WgUD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



        setContentView(R.layout.activity_main);
        responseView = (TextView) findViewById(R.id.responseView);
        photoView =(ImageView) findViewById(R.id.imageView);

        new RetrieveNasaPic().execute();
    }


    class RetrieveNasaPic extends AsyncTask<Void, Void, String>
    {

        private Exception exception;

        protected void onPreExecute() {
          //  progressBar.setVisibility(View.VISIBLE);
            responseView.setText("Loading...");
        }

        protected String doInBackground(Void... urls) {
          //  String email = emailText.getText().toString();
            // Do some validation here

            try {
                URL url = new URL(NASA_API_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response)
        {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
          //  progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            responseView.setText(response);
           // JSONObject obj = new JSONObject(response);

          //  photoView.setImageDrawable("https://crackberry.com/sites/crackberry.com/files/styles/large/public/topic_images/2013/ANDROID.png?itok=xhm7jaxS");
            // TODO: check this.exception
            // TODO: do something with the feed

           try {
                JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
                String photourl = object.getString("url");
                String phototitle =object.getString("title");
                setTitle(phototitle);
               // int likelihood = object.getInt("likelihood");
              //  JSONArray photos = object.getJSONArray("photos");
               Log.i("INFO",photourl);
               URL url = null;
               try {
                   url = new URL(photourl);
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               }

               Object content = url.getContent();
               InputStream is = (InputStream) content;
               Drawable d = Drawable.createFromStream(is, "src");
               photoView.setImageDrawable(d);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
           }
        }
    }
}



