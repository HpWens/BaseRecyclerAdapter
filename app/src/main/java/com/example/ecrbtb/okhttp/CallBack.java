package com.example.ecrbtb.okhttp;

/**
 * Created by Tamic on 2016-08-02.
 */
public abstract class CallBack {
    public void onStart() {
    }

    public void onCompleted() {
    }

    abstract public void onError(Throwable e);

    public void onProgress(int progress) {
    }

    abstract public void onSucess(String path, String name, long fileSize);
}
