package com.swift.dating.ui.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.swift.dating.R;
import com.swift.dating.common.ScreenUtils;

public class CropImage extends BaseActivity implements View.OnClickListener,
        CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener {

    private ImageView ivBack;
    private TextView tvCrop;
    private CropImageView mCropImageView;

    private Uri imageUri;
    private Bitmap croppedImage, photoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        getData();
        initialize();
        implementListeners();
        setCropImageView();

    }

    /*
    *** Method to get image uri from the intent
     */
    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imageUri = Uri.parse(extras.getString("imageUri"));
        }
    }

    /**
     * initialize all the views and objects
     */
    private void initialize() {
        mCropImageView = findViewById(R.id.CropImageView);
        ivBack = findViewById(R.id.iv_back);
        tvCrop = findViewById(R.id.tv_crop);
    }

    /**
     * implement all listeners
     */
    private void implementListeners() {
        ivBack.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
    }

    /**
     * method to set aspect ratio and sizes
     */
    private void setCropImageView() {
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
        mCropImageView.setImageUriAsync(imageUri);
        mCropImageView.setShowProgressBar(false);
        mCropImageView.setFixedAspectRatio(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

            case R.id.tv_crop:
                showLoading();
                mCropImageView.getCroppedImageAsync(ScreenUtils.getScreenWidth(mActivity), ScreenUtils.getScreenHeight(mActivity));
                tvCrop.setEnabled(false);
                break;
        }

    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }


    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {

    }

    /*
     ***  Method to Handle Crop Result
     */
    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            Bitmap bitmap = result.getBitmap();
            if (bitmap != null) {
                croppedImage = bitmap;
                new CropingImageAsync().execute();
            }
        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
        }
    }

    /**
     ***  Class to do cropping on background thread
     */
    class CropingImageAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            photoImage = croppedImage;
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            File photoFile = persistImage(CropImage.this, photoImage);
            Log.e("photoFile", photoFile.length() + "");
            setResult(RESULT_OK, new Intent().putExtra("Image", photoFile.getAbsolutePath()));
            tvCrop.setEnabled(true);
            hideLoading();
            finish();
        }
    }


    public static File persistImage(Context context, Bitmap bitmap) {
        File imageFile = null;
        if (bitmap != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            String imageFileName = timeStamp + ".png";
            File filesDir = context.getFilesDir();
            imageFile = new File(filesDir, imageFileName);
            OutputStream os;
            try {
                os = new FileOutputStream(imageFile);
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                Log.e("bitmapSize ", bitmap.getByteCount() + " ");
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e("bitmapException", "Error writing bitmap", e);
            }
        }
        return imageFile;
    }
}