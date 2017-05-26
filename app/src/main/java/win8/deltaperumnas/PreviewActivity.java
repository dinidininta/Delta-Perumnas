package win8.deltaperumnas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    ImageView mImageView;
    Bitmap bm, newImage, logo;
    String date, time;
    Button savePhoto;
    ProgressBar spinner;
    AVLoadingIndicatorView avi;
    //AlertDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_preview);

        mImageView = (ImageView) findViewById(R.id.gambarPreview);
        savePhoto = (Button) findViewById(R.id.saveButton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("Preview");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("");

        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        //spinner=(ProgressBar)findViewById(R.id.progressBar);
        //spinner.setVisibility(View.GONE);

        bm = BitmapFactory.decodeFile(CaptionActivity.file.getPath());
        logo = BitmapFactory.decodeResource(getResources(), R.drawable.perumnas_baru);

        Bitmap.Config config = bm.getConfig();
        int width = bm.getWidth();
        int height = bm.getHeight();

        newImage = Bitmap.createBitmap(width, height, config);

        Canvas c = new Canvas(newImage);
        c.drawBitmap(bm, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(255-CaptionActivity.opac);

        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(90);
        textPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));

        Paint logoPaint = new Paint();

        c.drawRect(0, height-height/3, width/2+400, height, paint);
        c.drawBitmap(logo, 0, 80+height-height/3, logoPaint);
        c.drawText(CaptionActivity.perusahaan, 300, 200+height-height/3, textPaint);
        c.drawText("Pekerjaan: "+CaptionActivity.pekerjaan, 0, 400+height-height/3, textPaint);
        c.drawText("Proyek: "+CaptionActivity.proyek, 0, 500+height-height/3, textPaint);
        c.drawText("Lokasi: "+CaptionActivity.lokasi, 0, 600+height-height/3, textPaint);
        c.drawText("Keterangan: "+CaptionActivity.keterangan, 0, 700+height-height/3, textPaint);
        c.drawText(date, width/2-100, 200+height-height/3, textPaint);
        c.drawText(time, width/2-100, 300+height-height/3, textPaint);

        mImageView.setImageBitmap(newImage);

        savePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avi.setVisibility(View.VISIBLE);
                avi.smoothToShow();
//                spinner.setVisibility(View.VISIBLE);
                File file = new File(CaptionActivity.file.getPath());
                file.delete();
                saveImageToExternalStorage(newImage);
                Toast.makeText(getApplicationContext(), "Berhasil menyimpan", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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

//            spinner.setVisibility(View.GONE);
            avi.setVisibility(View.INVISIBLE);
            avi.smoothToHide();
            return true;

        } catch (Exception e) {
            Log.e("saveToExternalStorage()", e.getMessage());
            return false;
        }
    }
}
