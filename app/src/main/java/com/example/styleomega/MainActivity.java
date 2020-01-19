package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailID,password;
    Button btnSignIn;
    TextView signUpLink;
    TextView adminLink;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    String databseType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailID=findViewById(R.id.editText_Email);
        password=findViewById(R.id.editText_Password);
        btnSignIn=findViewById(R.id.button_signin);
        signUpLink=findViewById(R.id.textView_createAccountLink);
        adminLink=findViewById(R.id.textView_adminLogin);
        progressBar=findViewById(R.id.progressBarLogin);
        firebaseAuth=FirebaseAuth.getInstance();

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminLink.getHint().toString().equalsIgnoreCase("Login As Admin?"))
                {
                    btnSignIn.setText("Login As Admin");
                    databseType="ADMIN";
                    adminLink.setHint("Login As User?");
                }
                else if(adminLink.getHint().toString().equalsIgnoreCase("Login As User?"))
                {
                    btnSignIn.setText("Login");
                    databseType="USER";
                    adminLink.setHint("Login As Admin?");
                }
                else
                    {
                        btnSignIn.setText("Login As Admin");
                        adminLink.setHint("Login As User?");
                    }

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });


    }

    private void login() {

    }
}
