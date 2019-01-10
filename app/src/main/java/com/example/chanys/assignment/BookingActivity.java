package com.example.chanys.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.List;

public class BookingActivity extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    private String GET_URL = "https://sports-tify.000webhostapp.com/select_court.php";
    public static final String TAG_MESSAGE3 = "com.example.chanys.assignment.MESSAGE3";
    private ProgressDialog pDialog;
    RequestQueue queue;
    TextView CourttextView;
    TextView TelephonetextView;
    TextView AddresstextView;
    private String SelectedCourt;

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        Intent intent = getIntent(); //Who called me?
        if(intent.hasExtra(com.example.chanys.assignment.SportActivity.TAG_MESSAGE2)) {
            SelectedCourt = intent.getStringExtra(com.example.chanys.assignment.SportActivity.TAG_MESSAGE2);
        }

        CourttextView = findViewById(R.id.courtTextView);
        TelephonetextView = findViewById(R.id.telephoneTextView);
        AddresstextView = findViewById(R.id.addressTextView);

        pDialog = new ProgressDialog(this);
        downloadCourt(getApplicationContext(), GET_URL);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void SelectBooking(View view) {
        Intent intent = new Intent(this, BookingConfirm.class);
        intent.putExtra(TAG_MESSAGE3, SelectedCourt);
        this.startActivity(intent);
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
            pDialog.setMessage("Displaying Information of the Court...");
        pDialog.show();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject courseResponse = (JSONObject) response.get(i);
                                String SportName = courseResponse.getString("sportname");
                                String CourtName = courseResponse.getString("courtname");
                                if(SelectedCourt.equals(CourtName)) {
                                    String CourtAddress = courseResponse.getString("courtaddress");
                                    String CourtTelephone = courseResponse.getString("courttelephone");
                                    Court court = new Court(SportName, CourtName, CourtAddress, CourtTelephone);
                                    CourttextView.setText(CourtName);
                                    TelephonetextView.setText(String.format("%s : %s", "Telephone No.", court.getCourtTelephone()));
                                    AddresstextView.setText(String.format("%s : %s", "Court Address", court.getCourtAddress()));
                                }
                            }
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
}
