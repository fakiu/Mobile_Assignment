package com.example.chanys.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    ListView listViewSport;
    List<Sport> caList;
    private ProgressDialog pDialog;
    private String SavedName;
    //TODO: Please update the URL to point to your own server
    private static String GET_URL = "https://sports-tify.000webhostapp.com/select_sport.php";
    public static final String TAG_MESSAGE = "com.example.chanys.assignment.MESSAGE";
    RequestQueue queue;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent(); //Who called me?

        listViewSport = (ListView) findViewById(R.id.listViewSport);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        downloadSport(getApplicationContext(), GET_URL);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE);
        SavedName = prefs.getString("Username", null);//"No name defined" is the default value.

        listViewSport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ((Sport) listViewSport.getItemAtPosition(position)).getSportName();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MenuActivity.this, SportActivity.class);
                intent.putExtra(TAG_MESSAGE, s);
                startActivity(intent);
            }
        });
    }

    public void SelectBookingHistory(View view){
        Intent intent = new Intent(MenuActivity.this, BookingHistory.class);
        intent.putExtra(TAG_MESSAGE, SavedName);
        startActivity(intent);
    }

    public void Logout(View view){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        NavUtils.navigateUpFromSameTask(this);
    }

    private boolean isConnected() {
        //Asking the user device for network status
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void downloadSport(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog.isShowing())
            pDialog.setMessage("Syn With Server...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            caList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject courseResponse = (JSONObject) response.get(i);
                                String SportName = courseResponse.getString("sportname");
                                Sport sport = new Sport(SportName);
                                caList.add(sport); //Add a course record to List
                            }
                            LoadSport();
                            if (pDialog.isShowing())
                                pDialog.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag(TAG);

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    public void LoadSport(){
        final SportAdapter adapter = new SportAdapter(this, R.layout.activity_menu , caList);
        listViewSport.setAdapter(adapter);
    }

}
