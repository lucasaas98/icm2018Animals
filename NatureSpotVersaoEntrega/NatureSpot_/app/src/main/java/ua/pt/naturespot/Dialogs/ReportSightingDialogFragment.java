package ua.pt.naturespot.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import ua.pt.naturespot.Model.SightingsData;

public class ReportSightingDialogFragment extends DialogFragment {

    private DatabaseReference dbref;
    private FirebaseDatabase mDatabase;
    private String emailCurrentUser;
    private SightingsData sd;

    public void setSd(SightingsData sd) {
        Log.d("UUID rsdf", sd.getUuidUser());
        this.sd = sd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance();
        emailCurrentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Report Sighting?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("reports");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //DataSnapshot here = dataSnapshot;
                                //String email = emailCurrentUser;
                                //String userId = mDatabase.push().getKey();
                                Date date = new Date();
                                String timestamp = Long.toString(date.getTime());
                                Log.d("afsdsdffw",sd.toString());
                                mDatabase.child(sd.getUuidUser()).child(timestamp).setValue(sd.getId_());
                                Toast.makeText(getContext(), "Report Added", Toast.LENGTH_SHORT).show();
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

