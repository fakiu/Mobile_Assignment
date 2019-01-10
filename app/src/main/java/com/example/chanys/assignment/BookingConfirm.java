package com.example.chanys.assignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookingConfirm extends AppCompatActivity {

    EditText editTextStartTime, editTextEndTime;
    Integer BookingDay, BookingMonth, BookingYear;
    String BookingID;
    String SavedName;
    String SelectedCourt;
    TextView textViewBookCourt, textViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);

        SharedPreferences prefs =   getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE);
        SavedName = prefs.getString("Username", null);//"No name defined" is the default value.

        Intent intent = getIntent();
        if(intent.hasExtra(com.example.chanys.assignment.BookingActivity.TAG_MESSAGE3)) {
            SelectedCourt = intent.getStringExtra(com.example.chanys.assignment.BookingActivity.TAG_MESSAGE3);
        }

        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        textViewBookCourt = findViewById(R.id.textViewBookCourt);
        textViewDate = findViewById(R.id.textViewBookDate);
        textViewBookCourt.setText(String.format("%s %s", "Booking Court for", SelectedCourt));
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),getString(R.string.datepicker));
    }

    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string +
                "/" + day_string + "/" + year_string);

        textViewDate.setText(String.format("%s %s", "Booking Date :", dateMessage));

        BookingDay = day;
        BookingMonth = month+1;
        BookingYear = year;
        BookingID = String.format("%s%s", SavedName, dateMessage);
        Toast.makeText(getApplicationContext(), BookingID, Toast.LENGTH_LONG).show();
    }

    public void saveRecord(View v) {
        BookingInformation booking = new BookingInformation();

        booking.setBookingID(BookingID);
        booking.setUsername(SavedName);
        booking.setCourtName(SelectedCourt);
        booking.setBookingDay(BookingDay.toString());
        booking.setBookingMonth(BookingMonth.toString());
        booking.setBookingYear(BookingYear.toString());
        booking.setStartTime(editTextStartTime.getText().toString());
        booking.setEndTime(editTextEndTime.getText().toString());

        try {
            //TODO: Please update the URL to point to your own server
            makeServiceCall(this, "https://sports-tify.000webhostapp.com/insert_booking.php", booking);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void makeServiceCall(Context context, String url, final BookingInformation booking) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        //Send data
        try {
            StringRequest postRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(response);
                                int success = jsonObject.getInt("success");
                                String message = jsonObject.getString("message");
                                if (success==0) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error. " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("bookingid", booking.getBookingID());
                    params.put("username", booking.getUsername());
                    params.put("courtname", booking.getCourtName());
                    params.put("bookingday", booking.getBookingDay());
                    params.put("bookingmonth", booking.getBookingMonth());
                    params.put("bookingyear", booking.getBookingYear());
                    params.put("starttime", booking.getStartTime());
                    params.put("endtime", booking.getEndTime());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
