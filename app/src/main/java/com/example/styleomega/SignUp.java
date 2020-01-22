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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    EditText Name,Email,PhoneNo,Password;
    Button btnSignUp;
    TextView signInLink;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

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
        final String email=Email.getText().toString().trim();
        final String name=Name.getText().toString().trim();
        final String phoneNo=PhoneNo.getText().toString().trim();
        final String password=Password.getText().toString().trim();




        if(TextUtils.isEmpty(name))
        {
            Name.setError("Name is required");

        }
        else if(TextUtils.isEmpty(email))
        {
           Email.setError("Email is required");
        }

        else if(TextUtils.isEmpty(phoneNo))
        {
            PhoneNo.setError("Phone No is required");
        }
        else if(TextUtils.isEmpty(password))
        {
            Password.setError("Password is required");
        }

        else if(password.length()<6)
        {
            Password.setError("Password must be more than 6 characters");
        }
        else

            {
                progressBar.setVisibility(View.VISIBLE);
                btnSignUp.setVisibility(View.INVISIBLE);

               AuthenticateEmail(email,name,phoneNo,password);
               //CompleteCreating(email,password);

            }


    }



    private void AuthenticateEmail(final String email,final String name,final String phoneNo,final String password) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phoneNo).exists())) {

                    HashMap<String,Object> userData = new HashMap<>();
                    userData.put("name",name);
                    userData.put("email",email);
                    userData.put("password",password);
                    userData.put("phone",phoneNo);



                    myRef.child("Users").child(phoneNo).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                Toast.makeText(SignUp.this, "Your account has been successfully created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,MainActivity.class));
                                finish();
                            }
                            else
                            {

                                Toast.makeText(SignUp.this, "Error occured when creating account", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp.this,MainActivity.class));
                                finish();



                            }
                        }
                    });

                } else {

                    Toast.makeText(SignUp.this, "This email address " + email + " has already been registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void CompleteCreating(final String email,final String password) {
//        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(SignUp.this, "Your account has been successfully created", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignUp.this,MainActivity.class));
////                  finish();
//
//                }
//                else
//                    {
//                        Toast.makeText(SignUp.this, "Error occured when creating account", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(SignUp.this,MainActivity.class));
////                        finish();
//
//                    }
//            }
//        });
//
//    }


}
