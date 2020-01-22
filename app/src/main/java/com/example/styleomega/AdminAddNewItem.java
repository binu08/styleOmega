package com.example.styleomega;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewItem extends AppCompatActivity {

    private String categoryName,pName,pDescription,pPrice,saveCurrentDate,saveCurrentTime;
    private Button addNewbtn;
    private TextView categoryLabel;
    private EditText productName,productDescription,productPrice;
    private ImageView selectImage;
    private static final int galleryPick=1;
    private Uri imageUri;
    private StorageReference productImageReference;
    private String randomKey,downloadImageUrl;
    private DatabaseReference myProductsRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_item);
        loadingBar = new ProgressDialog(this);
        categoryName=getIntent().getExtras().get("Category").toString();

        //where the product images will be saved
        productImageReference= FirebaseStorage.getInstance().getReference().child("Product Images");
        myProductsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        Toast.makeText(AdminAddNewItem.this,categoryName,Toast.LENGTH_SHORT).show();
        addNewbtn=findViewById(R.id.button_add__new_product);
        categoryLabel=findViewById(R.id.textView_show_category);
        categoryLabel.setText("Add New "+categoryName);
        productName=findViewById(R.id.product_name);
        productDescription=findViewById(R.id.product_description);
        productPrice=findViewById(R.id.product_price);
        selectImage=findViewById(R.id.select_product_image);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addNewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }

    private void validateProductData() {
        pName=productName.getText().toString().trim();
        pDescription=productDescription.getText().toString().trim();
        pPrice=productPrice.getText().toString().trim();

        if(imageUri==null)
        {
            Toast.makeText(this, "Product Image is Required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pName))
        {
            productName.setError("Please Enter Product Name");
        }
        else if(TextUtils.isEmpty(pDescription))
        {
            productDescription.setError("Please Enter Product Description");
        }
        else if(TextUtils.isEmpty(pPrice))
        {
            productPrice.setError("Please Enter Product Price");
        }
        else
            {
                loadingBar.setTitle("Adding New Product");
                loadingBar.setMessage("Hold on we are adding your information to datbase");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                storeProductInformation();
            }
    }

    //saving product information in firebase storage
    private void storeProductInformation() {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        randomKey=saveCurrentDate+saveCurrentTime;

        final StorageReference filePathForImages=productImageReference.child(imageUri.getLastPathSegment()+randomKey+".jpg");


        final UploadTask uploadTask = filePathForImages.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String ErrorMessage = e.toString();

               Toast.makeText(AdminAddNewItem.this, "Error Occurred when uploading image! " + ErrorMessage, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            Toast.makeText(AdminAddNewItem.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        downloadImageUrl = filePathForImages.getDownloadUrl().toString();
                        return filePathForImages.getDownloadUrl();


                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            //since the task is succesfull that means the uri here is the correct one
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewItem.this, "Getting Product Image URL successfully", Toast.LENGTH_SHORT).show();

                            saveProductInfoIntoDatabase();
                        }

                    }
                });
            }
        });



    }

    private void saveProductInfoIntoDatabase() {
        HashMap<String, Object> productInfoMap = new HashMap<>();

        productInfoMap.put("productName", pName);
        productInfoMap.put("pID",randomKey );
        productInfoMap.put("date", saveCurrentDate);
        productInfoMap.put("time", saveCurrentTime);
        productInfoMap.put("description", pDescription);
        productInfoMap.put("category", categoryName);
        productInfoMap.put("price", pPrice);
        productInfoMap.put("image", downloadImageUrl);




        myProductsRef.child(randomKey).updateChildren(productInfoMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {

                            Intent intent = new Intent(AdminAddNewItem.this,AdminCategory.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewItem.this, "Product is Successfully Added!", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String ErrorMessage = task.getException().toString();
                            Toast.makeText(AdminAddNewItem.this, "Error: " + ErrorMessage , Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    //its gonna ask user to select an image
    private void openGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,galleryPick);  //galleryPick is an final int code that we have to provide
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null)
        {
            imageUri=data.getData();
            selectImage.setImageURI(imageUri);
        }
    }
}
