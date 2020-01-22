package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class Home extends AppCompatActivity {
    Button logoutbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logoutbtn=findViewById(R.id.button_Home_logout);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 logout();
             }
         });


    }

    private void logout() {
        Paper.book().destroy(); //to logout
        startActivity(new Intent(Home.this,MainActivity.class));
        Toast.makeText(Home.this,"LogOut Successful",Toast.LENGTH_SHORT).show();
        finish();
    }
}
