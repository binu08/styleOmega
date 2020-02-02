package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity {

    Button dailerBtn,emailBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        dailerBtn=findViewById(R.id.button_contactUs_dailer_btn);
        emailBtn=findViewById(R.id.button_contactUs_email_btn);

        dailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call(v);
            }
        });

        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    public  void Call(View v)
    {

        // show() method display the toast with message
        Toast.makeText(this, "Redirecting to dialer", Toast.LENGTH_LONG)
                .show();

        // Use format with "tel:" and phoneNumber created is
        // stored in u.
//        Uri u = Uri.parse("tel:" + e.getText().toString());
        Uri u = Uri.parse("tel:0713304844");

        // Create the intent and set the data for the
        // intent as the phone number.
        Intent i = new Intent(Intent.ACTION_DIAL, u);

        try
        {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i);
        }
        catch (SecurityException s)
        {
            // show() method display the toast with
            // exception message.
            Toast.makeText(this, s.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void sendEmail()
    {
//        final Intent emailLauncher = new Intent(Intent.ACTION_VIEW);
//        emailLauncher.setType("message/rfc822");
////        emailLauncher.setData();
//        try{
//            startActivity(emailLauncher);
//        }catch(ActivityNotFoundException e){
//
//        }

//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("message/rfc822");
//        startActivity(Intent.createChooser(intent, "Style Omega"));

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:style.omega@yahoo.com"));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
           Toast.makeText(ContactUs.this,"An Email Application was not detected! ",Toast.LENGTH_SHORT).show();
        }

    }
}
