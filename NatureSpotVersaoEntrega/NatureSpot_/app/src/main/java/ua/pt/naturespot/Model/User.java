package ua.pt.naturespot.Model;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    public String name;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void saveUser(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // new user node would be /users/$userid/
        String userId = mDatabase.push().getKey();
        // Check for already existed userId
        if (TextUtils.isEmpty(userId)) {
            mDatabase.child(userId).setValue(this);
        }else{
            mDatabase.child(userId).setValue(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}