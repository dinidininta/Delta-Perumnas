package win8.deltaperumnas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptionActivity extends AppCompatActivity {
    public static Uri file;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Button saveCaption;
    TextView showOpacity;
    SeekBar opacity;
    EditText etperusahaan, etpekerjaan, etproyek, etlokasi, etketerangan;
    public static String perusahaan, pekerjaan, proyek, lokasi, keterangan;
    public static int opac=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_caption);

        saveCaption = (Button) findViewById(R.id.button_simpancaption);
        etperusahaan = (EditText) findViewById(R.id.etPerusahaan);
        etpekerjaan = (EditText) findViewById(R.id.etPekerjaan);
        etproyek = (EditText) findViewById(R.id.etProyek);
        etlokasi = (EditText) findViewById(R.id.etLokasi);
        etketerangan = (EditText) findViewById(R.id.etKeterangan);
        showOpacity = (TextView) findViewById(R.id.teksOpacity);
        opacity = (SeekBar) findViewById(R.id.seekOpacity);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("Add Caption");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("");

        opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                showOpacity.setText("Opacity: "+progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                opac = seekBar.getProgress();
            }
        });

        saveCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perusahaan = etperusahaan.getText().toString();
                pekerjaan = etpekerjaan.getText().toString();
                proyek = etproyek.getText().toString();
                lokasi = etlokasi.getText().toString();
                keterangan = etlokasi.getText().toString();
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            file = Uri.fromFile(getOutputMediaFile());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivity(intent);
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Delta Perumnas");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
}
