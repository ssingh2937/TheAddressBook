package com.sandhu.theaddressbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Objects;

public class ContactDetailsActivity extends AppCompatActivity {
    TextView nameText, phoneText, emailText;
    ImageView profileImage, backBtn;
    CardView phoneCard, emailCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        nameText = findViewById(R.id.contact_name);
        phoneText = findViewById(R.id.contact_phone);
        emailText = findViewById(R.id.contact_email);
        profileImage = findViewById(R.id.contact_image);
        phoneCard = findViewById(R.id.phone_card);
        emailCard = findViewById(R.id.email_card);
        backBtn = findViewById(R.id.back_btn);

        Intent intent = getIntent();
        String first_name = intent.getStringExtra("first_name");
        String last_name = intent.getStringExtra("last_name");
        String phone = intent.getStringExtra("phone");
        String email = intent.getStringExtra("email");
        byte[] image = intent.getByteArrayExtra("image");

        //avoiding null pointer
        try{
            byte[] imageByteArray = Base64.decode(image, Base64.DEFAULT);
            Glide.with(this).asBitmap()
                    .load(imageByteArray)
                    .into(profileImage);
        } catch (Exception e){
            Log.e("onCreate: ", e.getMessage());
        }

        nameText.setText(first_name + " " + last_name);
        phoneText.setText(phone);
        emailText.setText(email);

        phoneCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneText.getText().toString().trim()));
                startActivity(intent);
            }
        });

        emailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(emailIntent,""));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}