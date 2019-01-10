package com.example.chanys.assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class checkingAdminOrCust extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_admin_or_cust);
    }

    public void toAdmin(View view){
        Intent adminLoginPage = new Intent(this,AdminLoginPage.class);
        this.startActivity(adminLoginPage);
    }

    public void toCustomer(View view){
        Intent customerLoginPage = new Intent(this,MainActivity.class);
        this.startActivity((customerLoginPage));
    }

}
