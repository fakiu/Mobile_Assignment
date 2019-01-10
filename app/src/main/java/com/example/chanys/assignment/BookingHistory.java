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

public class BookingHistory extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    ListView listViewBookingHistory;
    List<BookingInformation> caList;
    private ProgressDialog pDialog;
    private String SavedName;
    private static String GET_URL = "https://sports-tify.000webhostapp.com/select_booking.php";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        Intent intent = getIntent(); //Who called me?
        if(intent.hasExtra(com.example.chanys.assignment.MenuActivity.TAG_MESSAGE)) {
            SavedName = intent.getStringExtra(com.example.chanys.assignment.MenuActivity.TAG_MESSAGE);
        }

        listViewBookingHistory = (ListView) findViewById(R.id.listViewBookingHistory);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        downloadBookingInformation(getApplicationContext(), GET_URL);
    }

    private boolean isConnected() {
        //Asking the user device for network status
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    private void downloadBookingInformation(Context context, String url) {
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
                                String BookingID = courseResponse.getString("bookingid");
                                String Username = courseResponse.getString("username");
                                if(Username.equals(SavedName)) {
                                    String CourtName = courseResponse.getString("courtname");
                                    String BookingDay = courseResponse.getString("bookingday");
                                    String BookingMonth = courseResponse.getString("bookingmonth");
                                    String BookingYear = courseResponse.getString("bookingyear");
                                    String StartTime = courseResponse.getString("starttime");
                                    String EndTime = courseResponse.getString("endtime");
                                    BookingInformation booking = new BookingInformation(BookingID, Username, CourtName, BookingDay, BookingMonth, BookingYear, StartTime, EndTime);
                                    caList.add(booking); //Add a course record to List
                                }
                            }
                            LoadBookingInformation();
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

    public void LoadBookingInformation(){
        final BookingHistoryAdapter adapter = new BookingHistoryAdapter(this, R.layout.activity_booking_history, caList);
        listViewBookingHistory.setAdapter(adapter);
    }
}
