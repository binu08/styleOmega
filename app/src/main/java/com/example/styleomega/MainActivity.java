package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

    EditText emailID,password;
    Button btnSignIn;
    TextView signUpLink;
    TextView adminLink;
    TextView forgotPasswordLink;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    CheckBox rememberMe;
    String databseType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailID=findViewById(R.id.editText_Register_Email);
        password=findViewById(R.id.editText_Register_password);
        btnSignIn=findViewById(R.id.button_signin);
        signUpLink=findViewById(R.id.textView_createAccountLink);
        adminLink=findViewById(R.id.textView_adminLogin);
        forgotPasswordLink=findViewById(R.id.textView_forgotPasswordLink);
        progressBar=findViewById(R.id.progressBarLogin);
        firebaseAuth=FirebaseAuth.getInstance();
        rememberMe=findViewById(R.id.checkBox_remeberMe);


        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminLink.getText().toString().equalsIgnoreCase("Login As Admin?"))
                {
                    btnSignIn.setText("Login As Admin");
                    databseType="ADMIN";
                    adminLink.setText("Login As User?");
                }
                else if(adminLink.getText().toString().equalsIgnoreCase("Login As User?"))
                {
                    btnSignIn.setText("Login");
                    databseType="USER";
                    adminLink.setText("Login As Admin?");
                }
                else
                    {
                        btnSignIn.setText("Login As Admin");
                        adminLink.setText("Login As User?");
                        databseType="USER";
                    }

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnSignIn.getText().equals("Login"))
                {
                    login();
                }
                else if(btnSignIn.getText().equals("Login As Admin")){
                    loginAsAdmin();

                }

            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });

        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail=new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDailog=new AlertDialog.Builder(v.getContext());
                passwordResetDailog.setTitle("Reset Your Password?");
                passwordResetDailog.setMessage("Enter your email to receive reset link!");
                passwordResetDailog.setView(resetMail);


                passwordResetDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //extract email
                        String email=resetMail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset Link was sent to your email! Please use the link to reset your password",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error! Rest Link was not sent "+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });

                passwordResetDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cannceling
                    }
                });
                passwordResetDailog.create().show();
            }
        });


    }

    private void loginAsAdmin() {
        btnSignIn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        String email=emailID.getText().toString().trim();
        String pass=password.getText().toString().trim();

        if(isEmpty(email))
        {
            emailID.setError("Email is required");
        }
        if(isEmpty(pass))
        {
            password.setError("Email is required");
        }


        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userID=firebaseAuth.getCurrentUser().getUid();
                    if(userID.equalsIgnoreCase("OlVcjMLkZdcUcRYUJUxjd0JdRsG3"))
                    {
                        Toast.makeText(MainActivity.this, "Welcome Admin.",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this,Home.class);
                        startActivity(intent);
                    }
                    else
                        {
                            Toast.makeText(MainActivity.this, "Email you provided does not belong to an Administrator account",Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                        }

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Error Occurred while logging in "+task.getException(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
//
                }
            }
        });
    }

    private void login() {
        btnSignIn.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

    String email=emailID.getText().toString().trim();
    String pass=password.getText().toString().trim();

        if(isEmpty(email))
        {
            emailID.setError("Email is required");
        }
        if(isEmpty(pass))
        {
            password.setError("Email is required");
        }

        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successful.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,Home.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Error Occurred while logging in "+task.getException(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    btnSignIn.setVisibility(View.VISIBLE);
//
                }
            }
        });
    }
}
