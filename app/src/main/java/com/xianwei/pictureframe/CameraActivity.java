package com.xianwei.pictureframe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

/**
 * Created by xianwei li on 11/24/2017.
 */

public class CameraActivity extends Activity {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private String checkedFrameName = "none";
    private String preVideoPath;
    private String videoPath;
    private FFmpeg ffmpeg;
    @BindView(R.id.button_capture)
    ImageButton captureBtn;
    @BindView(R.id.button_save)
    ImageButton saveBtn;
    @BindView(R.id.button_back)
    ImageButton backBtn;
    @BindView(R.id.button_add_frame)
    ImageButton addFrameBtn;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    FrameLayout preview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        ButterKnife.bind(this);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
    }

    @OnClick(R.id.button_capture)
    void captureVideo() {
        if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder
            switchToSaveBtn();
            // inform the user that recording has stopped
//            setCaptureButtonText("Capture");
            isRecording = false;
        } else {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                // inform the user that recording has started
//                setCaptureButtonText("Stop");
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }
    }

    @OnClick(R.id.button_save)
    void saveVideo() {
        Toast.makeText(this, "video processing", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.VISIBLE);
        AsyncTask<Void, Void, Void> task = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                addFrameAndSaveVideo();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        };
        task.execute();
    }

    private void addFrameAndSaveVideo() {
        loadFFmpeg();
        preVideoPath = videoPath;
        String[] cmdd = new String[] {
                "-i",
                videoPath,
                "-i",
                "/storage/emulated/0/waterMarker.png",
                "-filter_complex",
                "overlay=10:10",
                getOutputMediaFile(MEDIA_TYPE_VIDEO).toString()};
        try {
            ffmpeg.execute(cmdd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {
                    Log.i("12345", "onProgress");
                }

                @Override
                public void onFailure(String message) {
                    Log.i("12345", message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.i("12345", "onSuccess");
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(CameraActivity.this, SuccessActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onFinish() {
                    Log.i("12345", "onFinish");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
            Log.i("12345", " FFMPGE execute failed");
        }
    }

    private void loadFFmpeg() {
        ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {
                    Log.i("12345", "loadonSuccess");
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
            Log.i("12345", " FFMPGE loader failed");
        }
    }

    @OnClick(R.id.button_back)
    void backToPreviewActivity() {
        if (preVideoPath != null) {
            File file = new File(preVideoPath);
            file.delete();
        }
        finish();
    }

    @OnClick(R.id.button_add_frame)
    void addFrame() {
        FrameDialog frameDialog = FrameDialog.newInstance(checkedFrameName);
        frameDialog.show(getFragmentManager(),"checkFrame");
    }

    public void onSelectValue(String frameName) {
        checkedFrameName = frameName;
        Log.i("onSelectValue", frameName);
        setFrame(frameName);
    }

    public void setFrame(String name){
        switch (name) {
            case "black":
                preview.setForeground(getDrawable(R.drawable.frame_black));
                break;
            case "red":
                preview.setForeground(getDrawable(R.drawable.frame_red));
                break;
            case "green":
                preview.setForeground(getDrawable(R.drawable.frame_green));
                break;
            case "purple":
                preview.setForeground(getDrawable(R.drawable.frame_purple));
                break;
            default:
                preview.setForeground(null);
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PictureFrame");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("PictureFrame", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "frame.jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void switchToSaveBtn() {
        captureBtn.setVisibility(View.INVISIBLE);
        saveBtn.setVisibility(View.VISIBLE);
    }

    private void switchToCaptureBtn() {
        captureBtn.setVisibility(View.VISIBLE);
        saveBtn.setVisibility(View.INVISIBLE);
    }

    private boolean prepareVideoRecorder() {

        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        videoPath = getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(videoPath);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        mMediaRecorder.setMaxDuration(15000);

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
}