package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.styleomega.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
    private CircleImageView profileView;
    private EditText fullNameEditText, userPhoneEditText, addressEditText, passwordEditText, ConpasswordEditText;
    private TextView profileChangeText, closeTextBtn, AccountPhoneNumber;
    private Button saveTextBtn;
    private Switch showpasswords;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureRef;
    private String checkerForUpdatingAll = ""; //if user wants to change the whole profile including the photo
    private StorageTask uploadImageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        showpasswords = findViewById(R.id.switch1);
        AccountPhoneNumber = findViewById(R.id.current_account_phoneNo);
        profileView = findViewById(R.id.settings_profile_image);
        fullNameEditText = findViewById(R.id.settings_full_name);
        userPhoneEditText = findViewById(R.id.settings_phone_number);
        addressEditText = findViewById(R.id.settings_address);
        passwordEditText = findViewById(R.id.settings_password);
        ConpasswordEditText = findViewById(R.id.settings_Conpassword);
        profileChangeText = findViewById(R.id.change_settings_image_btn);
        closeTextBtn = findViewById(R.id.close_settings_btn);
        saveTextBtn = findViewById(R.id.Update_settings_btn);
        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        userInfoDisplay(profileView, fullNameEditText, userPhoneEditText, addressEditText, passwordEditText, AccountPhoneNumber);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkerForUpdatingAll.equals("clicked")) {
                    updateAllInfo();
                } else {

                    allowToUpdateInfo();
                }
            }
        });

        profileChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkerForUpdatingAll = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(Settings.this);

            }
        });

        showpasswords.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ConpasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                    {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ConpasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT |
                                InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            imageUri = result.getUri();

            profileView.setImageURI(imageUri);
        } else {


            Toast.makeText(this, "Error Occurred", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.this, Settings.class)); //refressing activity
            finish();
        }
    }

    private void allowToUpdateInfo() {
        String password = passwordEditText.getText().toString().trim();
        String Conpassword = ConpasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {

            userPhoneEditText.setError("Phone Number is Required!");
            //Toast.makeText(this, "Enter the phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            fullNameEditText.setError("Name is Required!");
            //            Toast.makeText(this, "Enter the address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            addressEditText.setError("Address is Required!");
//            Toast.makeText(this, "Enter the address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {

            passwordEditText.setError("Password is Required");
//            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Conpassword)) {

            passwordEditText.setError("Reenter Password is Required");
//            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else {

            if (password.equals(Conpassword)) {
                if (password.length() > 5) {
                    updateOnlyUserInfo();
                } else {
                    passwordEditText.setError("Password should contain at least 6 characters");

                }

            } else {
                passwordEditText.setError("Passwords do not match");
                ConpasswordEditText.setError("Please Retype the same password as above");
                Toast.makeText(Settings.this, "Password Confirmation Failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("name", fullNameEditText.getText().toString());
        userMap.put("address", addressEditText.getText().toString());
        userMap.put("phoneOrder", userPhoneEditText.getText().toString());
        userMap.put("password", passwordEditText.getText().toString());
        ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(Settings.this, Home.class));
        Toast.makeText(Settings.this, "Successfully Updated Profile Information!  ", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateAllInfo() {

        String password = passwordEditText.getText().toString().trim();
        String Conpassword = ConpasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userPhoneEditText.getText().toString())) {

            userPhoneEditText.setError("Phone Number is Required!");
            //Toast.makeText(this, "Enter the phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fullNameEditText.getText().toString())) {
            fullNameEditText.setError("Name is Required!");
            //            Toast.makeText(this, "Enter the address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(addressEditText.getText().toString())) {
            addressEditText.setError("Address is Required!");
//            Toast.makeText(this, "Enter the address", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {

            passwordEditText.setError("Password is Required");
//            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Conpassword)) {

            passwordEditText.setError("Reenter Password is Required");
//            Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
        } else if (checkerForUpdatingAll.equals("clicked")) {

            if (password.equals(Conpassword)) {
                if (password.length() > 5) {
                    updateImage();
                } else {
                    passwordEditText.setError("Password should contain at least 6 characters");

                }

            } else {
                passwordEditText.setError("Passwords do not match");
                ConpasswordEditText.setError("Please Retype the same password as above");
                Toast.makeText(Settings.this, "Password Confirmation Failed!", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(Settings.this, Settings.class)); //to refresh
//                    finish();
            }

        }

    }

    private void updateImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Your Profile");
        progressDialog.setMessage("Hold a moment, We are updating your account details");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {

            final StorageReference fileReference = storageProfilePictureRef.child(Prevalent.currentUser.getPhone() + ".jpg");

            uploadImageTask = fileReference.putFile(imageUri);

            uploadImageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        Toast.makeText(Settings.this, "Updating Profile Information Unccessful! \nError: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                    return fileReference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {

                                Uri downloadUrl = task.getResult();
                                myUrl = downloadUrl.toString();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("name", fullNameEditText.getText().toString());
                                userMap.put("address", addressEditText.getText().toString());
                                userMap.put("phoneOrder", userPhoneEditText.getText().toString());
                                userMap.put("password", passwordEditText.getText().toString());
                                userMap.put("image", myUrl);
                                ref.child(Prevalent.currentUser.getPhone()).updateChildren(userMap);
                                progressDialog.dismiss();
                                startActivity(new Intent(Settings.this, Home.class));
                                Toast.makeText(Settings.this, "Successfully Updated Profile Information!", Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(Settings.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else {

            Toast.makeText(this, "Image URL not selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView profileView,
                                 final EditText fullNameEditText, final EditText userPhoneEditText,
                                 final EditText addressEditText, final EditText passwordEditText, final TextView AccountPhoneNumber) {
        DatabaseReference UsersReference = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentUser.getPhone());
        UsersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("image").exists()) {
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String phoneOrder = dataSnapshot.child("phoneOrder").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phoneOrder);
                        addressEditText.setText(address);
                        passwordEditText.setText(password);
                        AccountPhoneNumber.setText("Account: " + phone);

                    }
                    else if (dataSnapshot.child("name").exists()&& dataSnapshot.child("phone").exists()&& dataSnapshot.child("password").exists()){
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String password = dataSnapshot.child("password").getValue().toString();

                        fullNameEditText.setText(name);
                        passwordEditText.setText(password);
                        AccountPhoneNumber.setText("Account: " + phone);
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
