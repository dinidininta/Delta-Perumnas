package win8.deltaperumnas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewActivity extends AppCompatActivity {

    ImageButton closeButton;
    ImageView mImageView;
    Bitmap bm, newImage, logo, resizedBm;
    String date, time;
    Button savePhoto;
    ProgressBar spinner;
    AVLoadingIndicatorView avi;
    String path="";
    //AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_preview);

        mImageView = (ImageView) findViewById(R.id.gambarPreview);
        closeButton = (ImageButton) findViewById(R.id.close_preview_button);
        savePhoto = (Button) findViewById(R.id.saveButton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        SharedPreferences sharedPreferences = this.getSharedPreferences("caption", 0);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        path = CaptionActivity.file.getPath();

        bm = BitmapFactory.decodeFile(path);
        logo = BitmapFactory.decodeResource(getResources(), R.drawable.perumnas_baru);

//        Bitmap.Config config = bm.getConfig();
        int width = bm.getWidth();
        int height = bm.getHeight();

        resizedBm = decodeSampledBitmapFromResource(path, width/2, height/2);
        Bitmap.Config config = resizedBm.getConfig();
        int resizedWidth = resizedBm.getWidth();
        int resizedHeight = resizedBm.getHeight();

        newImage = Bitmap.createBitmap(resizedWidth, resizedHeight, config);

        Canvas c = new Canvas(newImage);
        c.drawBitmap(resizedBm, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(255-CaptionActivity.opac);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(36);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));

        Paint logoPaint = new Paint();

        String allCap = "Pekerjaan: "+sharedPreferences.getString("pekerjaan", null) + "\n" +
                "Proyek: "+sharedPreferences.getString("proyek", null) + "\n" +
                "Lokasi: "+sharedPreferences.getString("lokasi", null) + "\n" +
                "Keterangan: "+sharedPreferences.getString("keterangan", null);
        String timestamp = "" + "\n" + date + "\n" + time;
        int capY = newImage.getHeight()-newImage.getHeight()/7;
        int timeY = newImage.getHeight()-newImage.getHeight()/3;
        c.drawRect(0, newImage.getHeight()-newImage.getHeight()/3, newImage.getWidth()-newImage.getWidth()/4, newImage.getHeight(), paint);
        c.drawBitmap(logo, 0, newImage.getHeight()-newImage.getHeight()/3, logoPaint);
        c.drawText(sharedPreferences.getString("perusahaan", null), logo.getWidth(), newImage.getHeight()-newImage.getHeight()/4, textPaint);
        for (String line : allCap.split("\n")){
            c.drawText(line, 0, capY, textPaint);
            capY += textPaint.descent() - textPaint.ascent();
        }
        for(String t : timestamp.split("\n")){
            c.drawText(t, newImage.getWidth()/10+newImage.getWidth()/2, timeY, textPaint);
            timeY += textPaint.descent() - textPaint.ascent();
        }

        mImageView.setImageBitmap(newImage);

        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSavePhoto();
            }
        });


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void doSavePhoto() {
        // TODO Auto-generated method stub
        final DialogLoading loading = new DialogLoading();
        FragmentManager fm = getSupportFragmentManager();
        loading.show(fm, "Loading");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //whatever you want just you have to launch overhere.
                saveImageToExternalStorage(newImage);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Berhasil menyimpan", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }

    public boolean saveImageToExternalStorage(Bitmap image) {
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Delta Perumnas");
            if (!dir.exists()) {
                dir.mkdirs();
            }


            OutputStream fOut = null;
            File file = new File(dir.getPath(), "IMG_"+ date + "_" + time + ".jpg");
            file.createNewFile();
            fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

            File fileToDel = new File(CaptionActivity.file.getPath());
            if(fileToDel.exists()){
                fileToDel.delete();
                path = null;
                bm.recycle();
            }
            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
