package com.github.library.indicator;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.github.library.indicator.LoadMoreType.APAY;
import static com.github.library.indicator.LoadMoreType.BALL_BEAT;
import static com.github.library.indicator.LoadMoreType.BALL_CLIP_ROTATE;
import static com.github.library.indicator.LoadMoreType.BALL_SCALE;

/**
 * Created by boby on 2017/1/22.
 */
@IntDef({APAY, BALL_BEAT, BALL_CLIP_ROTATE, BALL_SCALE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadMoreType {

    public static final int APAY = 0;

    public static final int BALL_BEAT = 1;

    public static final int BALL_CLIP_ROTATE = 2;

    public static final int BALL_SCALE = 3;

}
