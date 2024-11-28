package com.example.contactapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private ArrayList<Contact> contacts;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        }

        Contact contact = getItem(position);

        TextView nameTextView = convertView.findViewById(R.id.contactName);
        TextView phoneTextView = convertView.findViewById(R.id.contactPhone);
        TextView emailTextView = convertView.findViewById(R.id.contactEmail);
        ImageView photoImageView = convertView.findViewById(R.id.contactPhoto);

        nameTextView.setText(contact.getName() + " " + contact.getSurname());
        phoneTextView.setText(contact.getPhone());
        emailTextView.setText(contact.getEmail());

        // Load photo (assuming it's a URI to an image)
        if (contact.getPhotoUri() != null) {
            photoImageView.setImageURI(contact.getPhotoUri());
        }

        // Handle phone number click
        phoneTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contact.getPhone()));
            context.startActivity(intent);
        });

        // Handle email click
        emailTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + contact.getEmail()));
            context.startActivity(intent);
        });

        // Change background color if selected
        if (contact.isSelected()) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.selected_color)); // Choose a selection color
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.default_color)); // Default color
        }

        return convertView;
    }
}
