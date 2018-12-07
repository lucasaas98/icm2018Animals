package ua.pt.naturespot.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ua.pt.naturespot.Fragment.Fragment_Show_Sighting;
import ua.pt.naturespot.Model.SightingsData;
import ua.pt.naturespot.R;

public class MyAdapter extends RecyclerView.Adapter<ListViewHolder> {

    private Context mContext;
    //boolean flag;
    private List<SightingsData> mSightingsList;
    //OurData nice = new OurData();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");

    public MyAdapter(Context mContext, List<SightingsData> mSightingsList) {
        this.mContext = mContext;
        this.mSightingsList = mSightingsList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sightings, parent, false);
        //nice.startList();
        return new ListViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        //Log.d("tag", "I'm here!!!");
        StorageReference storageRef = storage.getReferenceFromUrl("gs://naturespot-3e30f.appspot.com");
        storageRef = storageRef.child(mSightingsList.get(position).getImage());
        //Log.d("tag", storageRef.getName());
        holder.getCardview();
        holder.card_view.setTag(position);
        //GifAnimationDrawable gif = null;

        //try {
        //    gif = new
        //            GifAnimationDrawable(mContext.getResources().openRawResource(R.raw.download));
        //   gif.setOneShot(false);
        //} catch (Resources.NotFoundException e) {
        //    e.printStackTrace();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

        //holder.mImage.setImageDrawable(gif);
        //gif.setVisible(true, true);
        //flag=false;
        //int size = 0;
        //if (nice.pictures != null)
        //    size = nice.pictures.size();
        //Log.d("SIZE: ", size+"");
        //if(position >= size)
            try {
                final File localFile = File.createTempFile("image", "jpg");
                //final int finalSize = size;
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        //Log.d("BYTES: ", ""+taskSnapshot.getBytesTransferred());
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        holder.mImage.setImageBitmap(bitmap);
                        //InputStream stream = null;
                        //try {
                        //    stream = mContext.getAssets().open("024039c80452f5313fef3b8bc6380a4c.gif");
                        //} catch (IOException e) {
                        //   e.printStackTrace();
                        //}
                        //GifAnimationDrawable gif ;


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("FAIL: ", "FAIL");
                    }
                });
                localFile.delete();
            } catch (IOException e ) {}
        //if(flag)
        //    holder.mImage.setImageBitmap(nice.pictures.get(position));
        //else{
        //    holder.mImage.setImageResource(R.drawable.x_725772);
        //}
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Fragment fragment = new Fragment_Show_Sighting();
                ((Fragment_Show_Sighting) fragment).setSd(mSightingsList.get(position));
                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.screen_area, fragment);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSightingsList.size();
    }
}

class ListViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    CardView card_view;
    View nice;


    ListViewHolder(View itemView) {
        super(itemView);
        nice = itemView;
        mImage = itemView.findViewById(R.id.ivImage);
        card_view = (CardView) itemView.findViewById(R.id.itemSightings);
    }

    public void getCardview(){
        card_view = (CardView) nice.findViewById(R.id.itemSightings);
    }
}
