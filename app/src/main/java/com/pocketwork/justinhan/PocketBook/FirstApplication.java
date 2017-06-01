package com.pocketwork.justinhan.PocketBook;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by justinhan on 5/29/17.
 */

public class FirstApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}
