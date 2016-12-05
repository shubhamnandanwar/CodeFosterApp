package com.clabs.codefosterapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostEventActivity extends AppCompatActivity {

    public static final int GALLERY_REQUEST = 1;
    private ImageButton addImageButton;
    private EditText postHeading;
    private EditText postDescription;
    private Uri imageUri;
    private ProgressDialog progressDialog;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.primary));
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Drawable upArrow = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.ic_keyboard_backspace_white_24dp);
            upArrow.mutate().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.primary), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Post Event");
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting...");

        addImageButton = (ImageButton) findViewById(R.id.add_image_button);
        postDescription = (EditText) findViewById(R.id.description_text_view);
        postHeading = (EditText) findViewById(R.id.heading_text_view);
        Button submitPost = (Button) findViewById(R.id.submit_post_button);
        submitPost.setOnClickListener(submitPostListener);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

    }

    View.OnClickListener submitPostListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            progressDialog.show();
            final String heading = postHeading.getText().toString().trim();
            final String desc = postDescription.getText().toString().trim();
            if (!TextUtils.isEmpty(heading) && !TextUtils.isEmpty(desc) && imageUri != null) {

                DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault());
                Date date = new Date();
                String str = dateFormat.format(date);
                String fileName = "Coder_" + str + ".jpg";
                StorageReference filePath = storageReference.child("Blob_Images").child(fileName);
                filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Snackbar.make(postDescription, "Successfully Posted", Snackbar.LENGTH_LONG).show();
                        if (downloadUri != null) {
                            EventPost eventPost = new EventPost(downloadUri.toString(), heading, desc);
                            DatabaseReference newPost = databaseReference.push();
                            newPost.setValue(eventPost);
                            progressDialog.dismiss();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(postDescription, "Posting Failed", Snackbar.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

            } else {
                Snackbar.make(postDescription, "Fill details", Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            imageUri = data.getData();
            addImageButton.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
