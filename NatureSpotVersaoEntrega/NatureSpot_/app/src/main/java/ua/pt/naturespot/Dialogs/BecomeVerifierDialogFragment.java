package ua.pt.naturespot.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BecomeVerifierDialogFragment extends DialogFragment {

    private DatabaseReference dbref;
    private FirebaseDatabase mDatabase;
    private String emailCurrentUser;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance();
        emailCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Send Verifier Request?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("requests");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                DataSnapshot here = dataSnapshot;
                                String email = emailCurrentUser;
                                String userId = mDatabase.push().getKey();
                                mDatabase.child(userId).setValue(email);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }



}

