package win8.deltaperumnas;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PreviewActivity extends AppCompatActivity {

    private Bitmap newImage;
    private String date, time, path;
    private AVLoadingIndicatorView avi;
    public static String EXTRA_FILE_PATH = "extra_file_path";
    private static final int PERMISSION_REQUEST_CODE = 200;
    //AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_preview);

        ImageView mImageView = (ImageView) findViewById(R.id.gambarPreview);
        ImageButton closeButton = (ImageButton) findViewById(R.id.close_preview_button);
        Button buttonSavePhoto = (Button) findViewById(R.id.saveButton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        SharedPreferences sharedPreferences = this.getSharedPreferences("caption", 0);
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

//        path = CaptionActivity.file.getPath();
        path = getIntent().getStringExtra(EXTRA_FILE_PATH);
        Log.i("cache path", path);

        Bitmap bm = BitmapFactory.decodeFile(path);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.perumnas_baru);

//        Bitmap.Config config = bm.getConfig();
        int width = bm.getWidth();
        int height = bm.getHeight();

        Bitmap resizedBm = decodeSampledBitmapFromResource(path, width / 2, height / 2);
        Bitmap.Config config = resizedBm.getConfig();
        int resizedWidth = resizedBm.getWidth();
        int resizedHeight = resizedBm.getHeight();

        newImage = Bitmap.createBitmap(resizedWidth, resizedHeight, config);

        Canvas c = new Canvas(newImage);
        c.drawBitmap(resizedBm, 0, 0, null);

        Log.i("newImageWidth", String.valueOf(newImage.getWidth()));
        Log.i("canvasWidth", String.valueOf(c.getWidth()));

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(255-CaptionActivity.opac);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(29);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));

        Paint logoPaint = new Paint();

        String allCap = "Pekerjaan: "+sharedPreferences.getString("pekerjaan", null) + "\n" +
                "Proyek: "+sharedPreferences.getString("proyek", null) + "\n" +
                "Lokasi: "+sharedPreferences.getString("lokasi", null) + "\n" +
                "Keterangan: "+sharedPreferences.getString("keterangan", null);
        String timestamp = "" + "\n" + date + "\n" + time;

        Log.i("keteranganLength",String.valueOf(textPaint.measureText("Keterangan: "
                +sharedPreferences.getString("keterangan", null))));

        int capY = newImage.getHeight()-newImage.getHeight()/7;
        int timeY = newImage.getHeight()-newImage.getHeight()/3;
//        float rectRight = getLongestSubstringLength(allCap.split("\n"), textPaint);
        float dtLength = getLongestSubstringLength(timestamp.split("\n"), textPaint);
//        float x = ((float) newImage.getWidth()) - rectRight;
//        float x = newImage.getWidth()-newImage.getWidth()/4;

        c.drawRect(0, newImage.getHeight()-newImage.getHeight()/3, newImage.getWidth(),
                newImage.getHeight(), paint);
//        c.drawRect(0, newImage.getHeight()-newImage.getHeight()/3, rectRight,
//                newImage.getHeight(), paint);
        c.drawBitmap(logo, 0, newImage.getHeight()-newImage.getHeight()/3, logoPaint);
        c.drawText(Objects.requireNonNull(sharedPreferences.getString("perusahaan", null)),
                logo.getWidth(), newImage.getHeight()-newImage.getHeight()/4, textPaint);
//        Log.i("boxWidth", String.valueOf(x));
        for (String line : allCap.split("\n")){
            c.drawText(line, 0, capY, textPaint);
            capY += textPaint.descent() - textPaint.ascent();
        }
        for(String t : timestamp.split("\n")){
//            c.drawText(t, newImage.getWidth()/9+newImage.getWidth()/2, timeY, textPaint);
            c.drawText(t, newImage.getWidth()-dtLength, timeY, textPaint);
            timeY += textPaint.descent() - textPaint.ascent();
        }

        mImageView.setImageBitmap(newImage);

        buttonSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhoto();
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

    private void savePhoto() {
        // TODO Auto-generated method stub
        final DialogLoading loading = new DialogLoading();
        FragmentManager fm = getSupportFragmentManager();
        loading.show(fm, "Loading");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                //whatever you want just you have to launch overhere.
                createFile();
                loading.dismiss();
//                Toast.makeText(getApplicationContext(), "Berhasil menyimpan", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }

    public void createFile() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Delta Perumnas");

        File file = new File(dir.getPath(), "IMG_"+ date.replace("-", "") +
                "_" + time.replace(":", "") + ".jpg");

        File delFile = new File(path);

        try {
            dir.mkdirs();
            OutputStream fOut = new FileOutputStream(file);

// 100 means no compression, the lower you go, the stronger the compression
            newImage.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();

//            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
//                    new MediaScannerConnection.OnScanCompletedListener() {
//                        @Override
//                        public void onScanCompleted(String s, Uri uri) {
//                            Log.i("createFile()", "Scanned " + s + ":");
//                            Log.i("createFile()", "-> uri=" + uri);
//                        }
//                    });

            delFile.delete();

            Toast.makeText(this, "Foto berhasil disimpan di: " + file.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("createFile()", e.getMessage());
//            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    private float getLongestSubstringLength(String[] texts, Paint p){
        float length = p.measureText(texts[0]);

        for(String text : texts){
            float temp = p.measureText(text);
            if(temp > length){
                length = temp;
            }
        }
        Log.i("getLongestSubstring()", String.valueOf(length));
        return length;
    }
}
