package com.example.chanys.assignment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdminLoginPage extends Activity {

    public static final String TAG_MESSAGE = "com.example.chanys.assignment.MESSAGE";
    private String GET_URL = "https://sports-tify.000webhostapp.com/select_adminlogin.php";
    private String UserID;
    private String Password;
    private boolean validate = false;
    private ProgressDialog progressDialog;
    EditText adminEditText1, adminPasswordEditText;
    private String SavedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
        }
        NetworkCalls.getInstance().setContext(getApplicationContext());
        NetworkCalls.getInstance().getRequestQueue();
        adminEditText1 = findViewById(R.id.adminEditText);
        adminPasswordEditText = findViewById(R.id.adminPassword);

    }
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs =   getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE);
        SavedName = prefs.getString("Username", null);//"No name defined" is the default value.
        String SavedPassword = prefs.getString("Password", null); //0 is the default value.
        String Court = prefs.getString("Courtname", null); //0 is the default value.
        if(SavedName != null && SavedPassword != null && Court != null){
            validate = true;
            validateACC();
        }
    }

    public void StartMain(View view) {
        Intent intent = new Intent(this, BookingHistory.class);
        startActivity(intent);
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void Login(View view){
        TextView temp = findViewById(R.id.adminEditText);
        UserID = temp.getText().toString();
        temp = findViewById(R.id.adminPassword);
        Password = temp.getText().toString();

        validateACC();
    }

    private void validateACC() {
        progressDialog = new ProgressDialog(AdminLoginPage.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Logging in"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                GET_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject accountRequest = (JSONObject) response.get(i);
                                String ID = accountRequest.getString("username");
                                String Pw = accountRequest.getString("password");
                                String Court = accountRequest.getString("courtname");
                                if(ID.equals(UserID) && Pw.equals(Password)) {
                                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("PrefText", MODE_PRIVATE).edit();
                                    editor.putString("Username", ID);
                                    editor.putString("Password", Pw);
                                    editor.putString("Courtname", Court);
                                    editor.commit();
                                    validate = true;
                                }
                            }
                            Proceed();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Set the tag on the request.
        jsonObjectRequest.setTag("sportify");

        // Add the request to the RequestQueue.
        NetworkCalls.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void Proceed(){
        progressDialog.dismiss();
        if(validate) {
            Intent intent = new Intent(AdminLoginPage.this, AdminBookingRecord.class);
            this.startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Incorrect Username or Password!",Toast.LENGTH_LONG).show();
        }
    }
}
