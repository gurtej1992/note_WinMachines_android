package com.tej.note_winmachines_android.Helper;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    MediaRecorder recorder = new MediaRecorder();
    public final String path;
    MediaPlayer mp = new MediaPlayer();

    public AudioRecorder(String path) {
        this.path = sanitizePath(path);
    }

    private String sanitizePath(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.getDownloadCacheDirectory().getAbsolutePath()
                    + path;
        }
        else{
            return Environment.getDownloadCacheDirectory().getAbsolutePath()
                    + path;
        }
    }

    public void start() throws IOException {
        String state = android.os.Environment.getExternalStorageState();
        if (!state.equals(android.os.Environment.MEDIA_MOUNTED)) {
            throw new IOException("SD Card is not mounted.  It is " + state
                    + ".");
        }

        // make sure the directory we plan to store the recording in exists
        File directory = new File(path).getParentFile();
        if (directory != null && !directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(path);
        recorder.prepare();
        recorder.start();

    }

    public void stop() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public void playOrStopRecording(String path) throws IOException {
        if(mp.isPlaying()){
            mp.stop();
        }
        else{
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
            mp.setVolume(10, 10);
        }
    }
}
