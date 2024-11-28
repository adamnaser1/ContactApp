package com.example.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView contactListView;
    private Button addButton, deleteButton;

    private ArrayList<Contact> contacts = new ArrayList<>();
    private ArrayList<Contact> selectedContacts = new ArrayList<>();
    private ContactAdapter adapter;
    private ContactDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing views
        contactListView = findViewById(R.id.contactListView);
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);

        dbHelper = new ContactDatabaseHelper(this); // Initialize database helper

        // Set up the list adapter before loading contacts
        adapter = new ContactAdapter(this, contacts);
        contactListView.setAdapter(adapter);

        // Load contacts from the database
        loadContacts();

        // Handle long press for selecting multiple contacts
        contactListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Contact selectedContact = contacts.get(position);
            if (!selectedContacts.contains(selectedContact)) {
                selectedContacts.add(selectedContact); // Add contact to selection
            } else {
                selectedContacts.remove(selectedContact); // Remove from selection
            }
            selectedContact.setSelected(!selectedContact.isSelected()); // Toggle selection
            adapter.notifyDataSetChanged(); // Update the list view with selection changes
            return true; // Return true to indicate the event was handled
        });

        // Handle single click for editing contact
        contactListView.setOnItemClickListener((parent, view, position, id) -> {
            if (selectedContacts.size() > 0) {
                // If multiple contacts are selected, do nothing for edit
                Toast.makeText(MainActivity.this, "You can only edit one contact at a time.", Toast.LENGTH_SHORT).show();
            } else {
                // Single selection, handle the action
                selectedContacts.clear();
                selectedContacts.add(contacts.get(position)); // Add to selectedContacts for editing
                Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
                intent.putExtra("contact", contacts.get(position)); // Pass the selected contact
                startActivityForResult(intent, 2); // For editing the selected contact
            }
        });

        // Handle add contact button click
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
            startActivityForResult(intent, 1); // For adding a new contact
        });

        // Handle delete selected contacts button click
        deleteButton.setOnClickListener(v -> {
            if (!selectedContacts.isEmpty()) {
                for (Contact contact : selectedContacts) {
                    dbHelper.deleteContact(contact.getId()); // Delete from database
                    contacts.remove(contact); // Remove from list
                }
                selectedContacts.clear(); // Clear selection after deletion
                adapter.notifyDataSetChanged(); // Notify the adapter about the change
                Toast.makeText(MainActivity.this, "Contacts deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please select contacts to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload contacts every time the activity is resumed to reflect changes made
        loadContacts();
    }

    // Method to load contacts from the database
    private void loadContacts() {
        contacts.clear();
        contacts.addAll(dbHelper.getAllContacts()); // Fetch contacts from the database
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Contact contact = (Contact) data.getSerializableExtra("contact");

            if (requestCode == 1) { // Adding new contact
                dbHelper.addContact(contact); // Save new contact to the database
            } else if (requestCode == 2) { // Editing existing contact
                dbHelper.updateContact(contact.getId(), contact); // Update the contact in the database
            }

            // Reload contacts from the database
            loadContacts();
        }
    }
}
