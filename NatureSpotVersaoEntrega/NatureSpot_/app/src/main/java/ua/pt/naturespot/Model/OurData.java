package ua.pt.naturespot.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

import ua.pt.naturespot.R;

public class OurData {
    public void startList(){
        this.pictures = new ArrayList<Bitmap>();
    }
    public static ArrayList<Bitmap> pictures;

    public static int[] picturePath = new int[] {
            R.drawable.pictureanimal3,
            R.drawable.pictureanimal4,
            R.drawable.pictureanimal1,
            R.drawable.pictureanimal2
    };
}
