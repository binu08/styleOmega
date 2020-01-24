package com.example.styleomega;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {
private CircleImageView profileView;
private EditText fullNameEditText,userPhoneEditText, addressEditText;
private TextView profileChangeText, closeTextBtn, saveTextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
