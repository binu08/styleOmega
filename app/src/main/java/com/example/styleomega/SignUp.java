package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    EditText Name,Email,PhoneNo,Password;
    Button btnSignUp;
    TextView signInLink;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Name=findViewById(R.id.editText_Name);
        Email=findViewById(R.id.editText_Email);
        PhoneNo=findViewById(R.id.editText_PhoneNumber);
        Password=findViewById(R.id.editText_Password);
        btnSignUp=findViewById(R.id.button_signin);
        signInLink=findViewById(R.id.textView_login);
        progressBar=findViewById(R.id.progressBarSignUp);
        firebaseAuth=FirebaseAuth.getInstance();

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);

            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               createAccount();
            }
        });
    }

    private void createAccount() {
        String name=Name.getText().toString().trim();
        String email=Email.getText().toString().trim();
        String phoneNo=PhoneNo.getText().toString().trim();
        String password=Password.getText().toString().trim();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        if(TextUtils.isEmpty(name))
        {
            Name.setError("Name is required");
            return;
        }
        if(TextUtils.isEmpty(email))
        {
           Email.setError("Email is required");
        }

        if(TextUtils.isEmpty(phoneNo))
        {
            PhoneNo.setError("Phone No is required");
        }
        if(TextUtils.isEmpty(password))
        {
            Password.setError("Password is required");
        }

        if(password.length()<6)
        {
            Password.setError("Password must be more than 6 characters");
        }

        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.INVISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignUp.this,"User Registration Successful",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUp.this,MainActivity.class));
                }
                else
                    {
                        Toast.makeText(SignUp.this,"User Registration UnSuccessful "+task.getException(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        btnSignUp.setVisibility(View.VISIBLE);
                    }
            }
        });

    }
}
