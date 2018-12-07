package ua.pt.naturespot.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import ua.pt.naturespot.R;
import ua.pt.naturespot.Model.SightingsData;

public class Fragment_Show_Sighting extends Fragment {
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SightingsData sd;
    public Fragment_Show_Sighting(){}

    public SightingsData getSd() {
        return sd;
    }

    public void setSd(SightingsData sd) {
        this.sd = sd;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showsighting, null);
        TextView name = (TextView) view.findViewById(R.id.editText3);
        TextView date = (TextView) view.findViewById(R.id.editText4);
        TextView location = (TextView) view.findViewById(R.id.editText5);
        TextView description = (TextView) view.findViewById(R.id.editText8);
        final de.hdodenhof.circleimageview.CircleImageView imageView2 = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.imageView);

        StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");
        storageRef = storageRef.child(sd.getImage());
        try {
            final File localFile = File.createTempFile("image", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("BYTES: ", ""+taskSnapshot.getBytesTransferred());
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView2.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d("FAIL: ", "FAIL");
                }
            });
            localFile.delete();
        } catch (IOException e ) {}

        name.setText(sd.getName());
        description.setText(sd.getDescription());
        date.setText(sd.getDate());
        location.setText(sd.getLocation());
        return view;
    }
}
