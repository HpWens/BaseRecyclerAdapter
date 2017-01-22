package com.github.baserecycleradapter.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.adapter.OrderMultipleAdapter;
import com.github.baserecycleradapter.entity.MultipleItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 7/27 0027.
 */
public class MultiItemActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private OrderMultipleAdapter mAdapter;

    private EditText etChat;
    private Button btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);

        btnSend = (Button) findViewById(R.id.btn_send);
        etChat = (EditText) findViewById(R.id.et_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter = new OrderMultipleAdapter(getMultipleItemData()));

    }


    /**
     * @return
     */
    public static List<MultipleItem> getMultipleItemData() {
        List<MultipleItem> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            if (i % 3 == 0) {

                MultipleItem item = new MultipleItem();
                item.itemType = MultipleItem.HEADER;
                list.add(item);

                MultipleItem item0 = new MultipleItem();
                item0.itemType = MultipleItem.CONTENT;
                item0.name = "01" + i;
                list.add(item0);

                MultipleItem item1 = new MultipleItem();
                item1.itemType = MultipleItem.CONTENT;
                item1.name = "02" + i;
                list.add(item1);

            } else if (i % 4 == 0) {

                MultipleItem item = new MultipleItem();
                item.itemType = MultipleItem.HEADER;
                list.add(item);

                MultipleItem item0 = new MultipleItem();
                item0.itemType = MultipleItem.CONTENT;
                item0.name = "01" + i;
                list.add(item0);

                MultipleItem item1 = new MultipleItem();
                item1.itemType = MultipleItem.CONTENT;
                item1.name = "02" + i;
                list.add(item1);

                MultipleItem item2 = new MultipleItem();
                item2.itemType = MultipleItem.CONTENT;
                item2.name = "03" + i;
                list.add(item2);

            } else {
                MultipleItem item = new MultipleItem();
                item.itemType = MultipleItem.HEADER;
                list.add(item);
            }
        }


        return list;
    }

    /**
     * @param bitmap 原图
     * @param pixels 圆角大小
     * @return
     */
    public Bitmap getRoundCornerBitmap(Bitmap bitmap, float pixels) {
        //获取bitmap的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap cornerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(cornerBitmap);
        paint.setAntiAlias(true);

        canvas.drawRoundRect(new RectF(0, 0, width, height), pixels, pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, new RectF(0, 0, width, height), paint);

        //绘制边框
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        paint.setColor(Color.GREEN);
        canvas.drawRoundRect(new RectF(0, 0, width, height), pixels, pixels, paint);

        return cornerBitmap;
    }


}
