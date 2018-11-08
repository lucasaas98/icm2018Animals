package ua.pt.naturespot;

import android.graphics.Bitmap;

public class BitmapScaler

{
    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float width2;
        width2 =(float) width/(float) 2.5;
        float factor = width2 / (float) b.getWidth() ;
        return Bitmap.createScaledBitmap(b, (int)width2, (int) (b.getHeight() * factor), true);
    }

    public static Bitmap scaleToFitHeight(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }
}
