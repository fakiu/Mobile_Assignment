package com.example.chanys.assignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class AdminBookingRecord extends AppCompatActivity {

    public static final String TAG = "my.edu.tarc.lab44";
    ListView listViewBookingHistory;
    List<BookingInformation> caList;
    private ProgressDialog pDialog;
    private String SelectedCourt;
    private static String GET_URL = "https://sports-tify.000webhostapp.com/select_booking.php";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_record);

        Intent intent = getIntent(); //Who called me?
        if(intent.hasExtra(com.example.chanys.assignment.AdminLoginPage.TAG_MESSAGE)) {
            SelectedCourt = intent.getStringExtra(com.example.chanys.assignment.AdminLoginPage.TAG_MESSAGE);
        }

        listViewBookingHistory = (ListView) findViewById(R.id.listViewBookingHistory);
        pDialog = new ProgressDialog(this);
        caList = new ArrayList<>();

        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(getApplicationContext(), SelectedCourt, Toast.LENGTH_LONG).show();
        downloadBookingInformation(getApplicationContext(), GET_URL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs =   getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE);
        SelectedCourt = prefs.getString("Courtname", null); //0 is the default value.
    }

    private boolean isConnected() {
        //Asking the user device for network status
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

    public void Logout(View view){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        NavUtils.navigateUpFromSameTask(this);
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
                                String CourtName = courseResponse.getString("courtname");
                                if(CourtName.equals(SelectedCourt)) {
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
        final AdminBookingHistoryAdapter adapter = new AdminBookingHistoryAdapter(this, R.layout.activity_admin_booking_record, caList);
        listViewBookingHistory.setAdapter(adapter);
    }

}
