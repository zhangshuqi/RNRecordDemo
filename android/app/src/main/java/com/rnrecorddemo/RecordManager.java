package com.rnrecorddemo;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.react.bridge.Promise;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    public void getRecodeFileList(Promise promise) {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        String paths = "/storage/emulated/0/Sounds/CallRecord";
        Log.d("tank", paths);
        Log.d("tank", externalStorageDirectory.getAbsolutePath() + "/Sounds/CallRecord");
        File file = new File(paths);
        Log.d("tank", externalStorageDirectory.getAbsolutePath());
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length <= 0) return;
            getAudioFileLength(files, promise);

        }
    }

    private void getAudioFileLength(File[] files, Promise promise) {
        Observable.create(new ObservableOnSubscribe<List<RecordFileBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<RecordFileBean>> emitter) throws Exception {
                List<RecordFileBean> recordFileBeanList = new ArrayList<>();
                for (File file1 : files) {
                    if (file1.isFile()) {
                        RecordFileBean recordFileBean = new RecordFileBean();
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(file1.getAbsolutePath());
                        String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        recordFileBean.duration = Long.parseLong(duration);
                        recordFileBean.filePath = file1.getAbsolutePath();
                        recordFileBeanList.add(recordFileBean);
                    }

                }
                emitter.onNext(recordFileBeanList);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<List<RecordFileBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<RecordFileBean> recordFileBeans) {
                Log.d("tank", JSON.toJSONString(recordFileBeans));
                promise.resolve(JSON.toJSONString(recordFileBeans));
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }


    public File getRecordFile() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        Log.d("tank", externalStorageDirectory.getAbsolutePath() + recordFilePath);
        File file = new File(getRecordFilePath());
        Log.d("tank", externalStorageDirectory.getAbsolutePath());
        return file;

    }

    public void  getRecodeFileList(Context context, Promise promise) {
        if (TextUtils.isEmpty(getRecordFilePath())) {
            Toast.makeText(context, "获取record file path 路径为null", Toast.LENGTH_SHORT).show();
            return  ;
        }

        File file = getRecordFile();
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length <= 0) return  ;
            getAudioFileLength(files,promise);

        }

    }

    public void removeFile(List<String> filePathList) {

        for (String s : filePathList) {
            new File(s).delete();

        }
    }
}
