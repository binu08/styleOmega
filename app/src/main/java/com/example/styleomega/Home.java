package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnSignIn=findViewById(R.id.button_Home_logout);

         btnSignIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 logout();
             }
         });


    }

    private void logout() {
        startActivity(new Intent(Home.this,MainActivity.class));
        Toast.makeText(Home.this,"LogOut Successful",Toast.LENGTH_SHORT).show();
        finish();
    }
}
