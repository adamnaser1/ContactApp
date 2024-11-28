package com.example.contactapp;

import android.net.Uri;
import java.io.Serializable;

public class Contact implements Serializable {
    private int id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Uri photoUri;
    private boolean isSelected; // New field for selection

    // Constructor
    public Contact(int id, String name, String surname, String phone, String email, Uri photoUri) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.photoUri = photoUri;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
