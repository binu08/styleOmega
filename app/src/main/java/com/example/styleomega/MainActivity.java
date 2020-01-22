package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Model.User;
import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static android.text.TextUtils.isEmpty;

public class MainActivity extends AppCompatActivity {

    EditText phoneNo, password;
    Button btnSignIn;
    TextView signUpLink;
    TextView adminLink;
    TextView forgotPasswordLink;
    ProgressDialog loadingBar;
    FirebaseAuth firebaseAuth;
    CheckBox rememberMe;
    String databaseType = "Users";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNo = findViewById(R.id.editText_Register_PhoneNo);
        password = findViewById(R.id.editText_Register_password);
        btnSignIn = findViewById(R.id.button_signin);
        signUpLink = findViewById(R.id.textView_createAccountLink);
        adminLink = findViewById(R.id.textView_adminLogin);
        forgotPasswordLink = findViewById(R.id.textView_forgotPasswordLink);
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        rememberMe = findViewById(R.id.checkBox_remeberMe);
        Paper.init(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();


        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminLink.getText().toString().equalsIgnoreCase("Login As Admin?")) {
                    btnSignIn.setText("Login As Admin");
                    databaseType = "Admins";
                    rememberMe.setVisibility(View.INVISIBLE);
                    adminLink.setText("Login As User?");
                } else if (adminLink.getText().toString().equalsIgnoreCase("Login As User?")) {
                    btnSignIn.setText("Login");
                    databaseType = "Users";
                    rememberMe.setVisibility(View.VISIBLE);
                    adminLink.setText("Login As Admin?");
                } else {
                    btnSignIn.setText("Login As Admin");
                    adminLink.setText("Login As User?");
                    databaseType = "Users";
                }

            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = phoneNo.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (isEmpty(phoneNumber)) {
                    phoneNo.setError("PhoneNo is required");
                } else if (isEmpty(pass)) {
                    password.setError("Password is required");
                } else {

                    loadingBar.setTitle("Login Account");
                    loadingBar.setMessage("Please Wait! Authenticating");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    AllowLogin(phoneNumber, pass);

                }


            }
        });

        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);

            }
        });

//        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final EditText resetMail = new EditText(v.getContext());
//                final AlertDialog.Builder passwordResetDailog = new AlertDialog.Builder(v.getContext());
//                passwordResetDailog.setTitle("Reset Your Password?");
//                passwordResetDailog.setMessage("Enter your email to receive reset link!");
//                passwordResetDailog.setView(resetMail);
//
//
//                passwordResetDailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //extract email
//                        String email = resetMail.getText().toString();
//                        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(MainActivity.this, "Reset Link was sent to your email! Please use the link to reset your password", Toast.LENGTH_LONG).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(MainActivity.this, "Error! Rest Link was not sent " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            }
//                        });
//
//                    }
//                });
//
//                passwordResetDailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //cannceling
//                    }
//                });
//                passwordResetDailog.create().show();
//            }
//        });

        String userPhoneNo = Paper.book().read(Prevalent.userPhoneNo);
        String userPassword = Paper.book().read(Prevalent.userPassword);



        if (userPassword != "" && userPhoneNo != "") {


            if (!TextUtils.isEmpty(userPhoneNo) && !TextUtils.isEmpty(userPassword)) {

                loadingBar.setTitle("Logging In using Remember Me");
                loadingBar.setMessage("Hold a moment");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();


                AllowLoginFromRemeberMe(userPhoneNo, userPassword);   //remember me function

            }

        }
    }

    private void AllowLogin(final String phoneNo, final String password) {

        if (rememberMe.isChecked()) {
            Paper.book().write(Prevalent.userPhoneNo, phoneNo);
            Paper.book().write(Prevalent.userPassword, password);

        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(databaseType).child(phoneNo).exists()) {
                    User user = dataSnapshot.child(databaseType).child(phoneNo).getValue(User.class);
                    if (user.getPhone().equals(phoneNo)) {
                        if (user.getPassword().equals(password)) {
                            if (databaseType.equals("Users")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Logged in as " + user.getName(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, Home.class));
                            } else if (databaseType.equals("Admins")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Logged in as ADMIN " + user.getName(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, AdminHome.class));
                            }

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Please enter valid Password ", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Please enter valid credentials", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Phone No with this number " + phoneNo + " does not exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void AllowLoginFromRemeberMe(final String phoneNo, final String password) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(databaseType).child(phoneNo).exists()) {
                    User user = dataSnapshot.child(databaseType).child(phoneNo).getValue(User.class);
                    if (user.getPhone().equals(phoneNo)) {
                        if (user.getPassword().equals(password)) {
                            if (databaseType.equals("Users")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Welcome Back, " + user.getName(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, Home.class));
                            } else if (databaseType.equals("Admins")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Welcome Back ADMIN, " + user.getName(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(MainActivity.this, AdminHome.class));
                            }

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password Incorrect! Error Occurred! while trying to login using cached data", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        loadingBar.dismiss();
                        Toast.makeText(MainActivity.this, "Error Occurered! while trying to login using cached data", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Phone Number does not exist! Error Occurred! while trying to login using cached data", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}









