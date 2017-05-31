package win8.deltaperumnas;

import android.content.Context;
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
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptionActivity extends AppCompatActivity {
    public static Uri file;
    private static final String TAG = "RegisterActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Vibrator vib;
    Animation animShake;
    Button saveCaption,lanjutkanButton;
    ImageButton backButton;
    TextView showOpacity;
    SeekBar opacity;
    private TextInputLayout perusahaanLayout, pekerjaanLayout, proyekLayout, lokasiLayout, keteranganlayout;
    private EditText etperusahaan, etpekerjaan, etproyek, etlokasi, etketerangan;
    public static String perusahaan, pekerjaan, proyek, lokasi, keterangan;
    public static int opac=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_caption);


        perusahaanLayout = (TextInputLayout) findViewById(R.id.perusahaan_layout);
        pekerjaanLayout = (TextInputLayout) findViewById(R.id.pekerjaan_layout);
        proyekLayout = (TextInputLayout) findViewById(R.id.proyek_layout);
        lokasiLayout = (TextInputLayout) findViewById(R.id.lokasi_layout);
        keteranganlayout = (TextInputLayout) findViewById(R.id.keterangan_layout);

        saveCaption = (Button) findViewById(R.id.button_simpancaption);
        lanjutkanButton = (Button) findViewById(R.id.lanjutkan_caption__button);
        backButton = (ImageButton) findViewById(R.id.back_caption_button);

        etperusahaan = (EditText) findViewById(R.id.etPerusahaan);
        etpekerjaan = (EditText) findViewById(R.id.etPekerjaan);
        etproyek = (EditText) findViewById(R.id.etProyek);
        etlokasi = (EditText) findViewById(R.id.etLokasi);
        etketerangan = (EditText) findViewById(R.id.etKeterangan);
        showOpacity = (TextView) findViewById(R.id.teksOpacity);
        opacity = (SeekBar) findViewById(R.id.seekOpacity);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        SharedPreferences sharedPreferences = this.getSharedPreferences("caption", 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        etperusahaan.setText(sharedPreferences.getString("perusahaan", null));
        etpekerjaan.setText(sharedPreferences.getString("pekerjaan", null));
        etproyek.setText(sharedPreferences.getString("proyek", null));
        etlokasi.setText(sharedPreferences.getString("lokasi", null));
        etketerangan.setText(sharedPreferences.getString("keterangan", null));

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
                editor.putString("opacity", String.valueOf(seekBar.getProgress()));
                opac = seekBar.getProgress();
            }
        });


        saveCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
        }
        });


        lanjutkanButton.setEnabled(false);

//        etperusahaan.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if(charSequence.toString().equals("")){
//                    lanjutkanButton.setEnabled(false);
//                }else {
//                    lanjutkanButton.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        etpekerjaan.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if(charSequence.toString().equals("")){
//                    lanjutkanButton.setEnabled(false);
//                }else {
//                    lanjutkanButton.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//
//        etproyek.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if(charSequence.toString().equals("")){
//                    lanjutkanButton.setEnabled(false);
//                }else {
//                    lanjutkanButton.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        etlokasi.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if(charSequence.toString().equals("")){
//                    lanjutkanButton.setEnabled(false);
//                }else {
//                    lanjutkanButton.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//
//        etketerangan.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if(charSequence.toString().equals("")){
//                    lanjutkanButton.setEnabled(false);
//                }else {
//                    lanjutkanButton.setEnabled(true);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        lanjutkanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("perusahaan", etperusahaan.getText().toString());
                editor.putString("pekerjaan", etpekerjaan.getText().toString());
                editor.putString("proyek", etproyek.getText().toString());
                editor.putString("lokasi", etlokasi.getText().toString());
                editor.putString("keterangan", etketerangan.getText().toString());
                editor.apply();
                dispatchTakePictureIntent();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void submitForm() {

        if (!checkPerusahaan()) {
            etperusahaan.setAnimation(animShake);
            etperusahaan.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPekerjaan()) {
            etpekerjaan.setAnimation(animShake);
            etpekerjaan.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkProyek()) {
            etproyek.setAnimation(animShake);
            etproyek.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkLokasi()) {
            etlokasi.setAnimation(animShake);
            etlokasi.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        if (!checkKeterangan()) {
            etketerangan.setAnimation(animShake);
            etketerangan.startAnimation(animShake);
            vib.vibrate(120);
            return;}

        perusahaanLayout.setErrorEnabled(false);
        pekerjaanLayout.setErrorEnabled(false);
        proyekLayout.setErrorEnabled(false);
        lokasiLayout.setErrorEnabled(false);
        keteranganlayout.setErrorEnabled(false);

        lanjutkanButton.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Berhasil disimpan", Toast.LENGTH_SHORT).show();
    }

    private boolean checkPerusahaan() {
        if (etperusahaan.getText().toString().trim().isEmpty()) {
            perusahaanLayout.setErrorEnabled(true);
           perusahaanLayout.setError(getString(R.string.err_msg_perusahaan));
            etperusahaan.setError(getString(R.string.err_msg_required));
            return false;
        }
        perusahaanLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkPekerjaan() {
        if (etpekerjaan.getText().toString().trim().isEmpty()) {
            pekerjaanLayout.setErrorEnabled(true);
           pekerjaanLayout.setError(getString(R.string.err_msg_pekerjaan));
            etpekerjaan.setError(getString(R.string.err_msg_required));
            return false;
        }
        pekerjaanLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkProyek() {
        if (etproyek.getText().toString().trim().isEmpty()) {
            proyekLayout.setErrorEnabled(true);
           proyekLayout.setError(getString(R.string.err_msg_proyek));
            etproyek.setError(getString(R.string.err_msg_required));
            return false;
        }
        proyekLayout.setErrorEnabled(false);
        return true;
    }

    private boolean checkLokasi() {
        if (etlokasi.getText().toString().trim().isEmpty()) {
            lokasiLayout.setErrorEnabled(true);
            lokasiLayout.setError(getString(R.string.err_msg_lokasi));
            etlokasi.setError(getString(R.string.err_msg_required));
            return false;

        }
        lokasiLayout.setErrorEnabled(false);
        return true;

    }

    private boolean checkKeterangan() {
        if (etketerangan.getText().toString().trim().isEmpty()) {
            keteranganlayout.setErrorEnabled(true);
            keteranganlayout.setError(getString(R.string.err_msg_keterangan));
            etketerangan.setError(getString(R.string.err_msg_required));
            return false;
        }
        keteranganlayout.setErrorEnabled(false);
        return true;
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
