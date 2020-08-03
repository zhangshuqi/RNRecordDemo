package com.rnrecorddemo;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecordManager {
    private String recordFilePath;


    private static class RecodeManagerSingleton {
        private static final RecordManager INSTANCE = new RecordManager();

    }

    public static RecordManager getInstance() {
        return RecodeManagerSingleton.INSTANCE;
    }

    public void setRecordFilePath(String filePath) {
        this.recordFilePath = filePath;
    }

    public String getRecordFilePath() {
        return recordFilePath;
    }

    private RecordManager() {

    }

    public void getRecodeFileList(String path) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String paths = "/storage/emulated/0/Sounds/CallRecord";
        Log.d("tank", paths);
        Log.d("tank", externalStorageDirectory.getAbsolutePath() + "/Sounds/CallRecord");
        File file = new File(paths);
        Log.d("tank", externalStorageDirectory.getAbsolutePath());
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                Log.d("tank", file1.getName());
            }
        }
    }

    public File getRecordFile() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Log.d("tank", externalStorageDirectory.getAbsolutePath() + recordFilePath);
        File file = new File(getRecordFilePath());
        Log.d("tank", externalStorageDirectory.getAbsolutePath());
        return file;

    }

    public List<String> getRecodeFileList(Context context) {
        if (TextUtils.isEmpty(getRecordFilePath())) {
            Toast.makeText(context, "获取record file path 路径为null", Toast.LENGTH_SHORT).show();
            return null;
        }
        List<String> filePathList = new ArrayList<>();

        File file = getRecordFile();
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isFile()) {
                    filePathList.add(file.getAbsolutePath());
                }
            }
        }
        return filePathList;
    }

    public void removeFile(List<String> filePathList) {

        for (String s : filePathList) {
            new File(s).delete();

        }
    }
}
