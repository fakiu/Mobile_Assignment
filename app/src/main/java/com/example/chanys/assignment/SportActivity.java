package com.example.chanys.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class SportActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    ListView listViewCourt;
    List<Court> caList;
    private String GET_URL = "https://sports-tify.000webhostapp.com/select_court.php";
    private ProgressDialog pDialog;
    public static final String TAG_MESSAGE2 = "com.example.chanys.assignment.MESSAGE2";
    TextView textView;
    private String SelectedSport;
    RequestQueue queue;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //Intent intent = new Intent(this, BookingActivity.class);
        //startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent(); //Who called me
        if(intent.hasExtra(com.example.chanys.assignment.MenuActivity.TAG_MESSAGE)) {
            SelectedSport = intent.getStringExtra(com.example.chanys.assignment.MenuActivity.TAG_MESSAGE);
        }

        listViewCourt = (ListView) findViewById(R.id.listViewCourt);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();
        textView = findViewById(R.id.SportTextView);
        textView.setText(SelectedSport);

        downloadCourt(getApplicationContext(), GET_URL);

        listViewCourt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ((Court) listViewCourt.getItemAtPosition(position)).getCourtName();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(SportActivity.this, BookingActivity.class);
                intent.putExtra(TAG_MESSAGE2, s);
                startActivity(intent);
            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void downloadCourt(Context context, String url) {
        // Instantiate the RequestQueue
        queue = Volley.newRequestQueue(context);

        if (!pDialog.isShowing())
            pDialog.setMessage("Searching Nearby Court...");
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
                                if(SelectedSport.equals(SportName)) {
                                    String CourtName = courseResponse.getString("courtname");
                                    String CourtAddress = courseResponse.getString("courtaddress");
                                    String CourtTelephone = courseResponse.getString("courttelephone");
                                    Court court = new Court(SportName, CourtName, CourtAddress, CourtTelephone);
                                    caList.add(court); //Add a course record to List
                                }
                            }
                            LoadCourt();
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

    public void LoadCourt(){
        final CourtAdapter adapter = new CourtAdapter(this, R.layout.activity_sport, caList);
        listViewCourt.setAdapter(adapter);
    }


}
