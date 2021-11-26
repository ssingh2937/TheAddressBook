package com.sandhu.theaddressbook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sandhu.theaddressbook.adapters.DatabaseAdapter;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactActivity extends AppCompatActivity {
     CircleImageView profileImage;
     EditText firstNameEdt, lastNameEdt, phoneEdt, emailEdt;
     Button saveBtn;
     DatabaseAdapter databaseAdapter;
     ImageView backBtn;
     public static final int PICK_IMAGE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Objects.requireNonNull(getSupportActionBar()).hide();

        databaseAdapter = new DatabaseAdapter(this);

        profileImage = findViewById(R.id.add_profile_image);
        firstNameEdt = findViewById(R.id.first_name_edt);
        lastNameEdt = findViewById(R.id.last_name_edt);
        phoneEdt = findViewById(R.id.phone_edt);
        emailEdt = findViewById(R.id.email_edt);
        saveBtn = findViewById(R.id.save_contact);
        backBtn = findViewById(R.id.back_btn);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEdt.getText().toString().trim();
                String lastName = lastNameEdt.getText().toString().trim();
                String phone = phoneEdt.getText().toString().trim();
                String email = emailEdt.getText().toString().trim();
                Bitmap image = getBitmapFromImageView(profileImage);
                byte[] imageArray = convertBitmapToByteArray(image);

                if(firstNameEdt.getText().toString().trim().equalsIgnoreCase("")){
                    firstNameEdt.setError("Please enter your first name");
                } else if(phoneEdt.getText().toString().trim().equalsIgnoreCase("")){
                    phoneEdt.setError("Please enter your phone number");
                } else {
                    databaseAdapter.insertData(firstName, lastName, phone, email, imageArray);
                    Toast.makeText(AddContactActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            profileImage.setImageURI(uri);
        }
    }

    public Bitmap getBitmapFromImageView(ImageView image){
        BitmapDrawable bitmapDrawable =(BitmapDrawable) profileImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        return bitmap;
    }

    public byte[] convertBitmapToByteArray(Bitmap yourBitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        yourBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    public void emptyEditTexts(){
        firstNameEdt.setText("");
        lastNameEdt.setText("");
        phoneEdt.setText("");
        emailEdt.setText("");
    }

}
