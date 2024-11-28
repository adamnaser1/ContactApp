package com.example.contactapp;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class EditContactActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText nameEditText, surnameEditText, phoneEditText, emailEditText;
    private ImageView photoImageView;
    private Button saveButton;
    private Uri photoUri;
    private Contact contact;
    private ContactDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        dbHelper = new ContactDatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        photoImageView = findViewById(R.id.contactPhoto);
        saveButton = findViewById(R.id.saveButton);

        // Get the passed contact (if editing)
        contact = (Contact) getIntent().getSerializableExtra("CONTACT");

        if (contact != null) {
            // Fill fields if editing an existing contact
            nameEditText.setText(contact.getName());
            surnameEditText.setText(contact.getSurname());
            phoneEditText.setText(contact.getPhone());
            emailEditText.setText(contact.getEmail());
            photoUri = contact.getPhotoUri();
            if (photoUri != null) {
                photoImageView.setImageURI(photoUri);
            }
        }

        // Handle photo selection
        photoImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        saveButton.setOnClickListener(v -> saveContact());
    }

    private void saveContact() {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();

        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Save the contact
            if (contact == null) {
                // New contact
                contact = new Contact(0, name, surname, phone, email, photoUri);
                long id = dbHelper.addContact(contact);
                if (id != -1) {
                    Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Update existing contact
                contact = new Contact(contact.getId(), name, surname, phone, email, photoUri);
                int rowsUpdated = dbHelper.updateContact(contact.getId(), contact);
                if (rowsUpdated > 0) {
                    Toast.makeText(this, "Contact updated", Toast.LENGTH_SHORT).show();
                }
            }
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                photoUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                    photoImageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
