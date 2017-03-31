package com.github.library.listener;

/**
 * Created by boby on 2017/3/31.
 */

public interface BaseRefreshListener {

    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();
}
