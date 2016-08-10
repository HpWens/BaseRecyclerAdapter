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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.entity.MultiItem;
import com.github.library.BaseMultiItemAdapter;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 7/27 0027.
 */
public class MultiItemActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<MultiItem> mAdapter;

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

        mRecyclerView.setAdapter(mAdapter = new BaseMultiItemAdapter<MultiItem>(this, getMultiItemDatas()) {
            @Override
            protected void convert(BaseViewHolder helper, MultiItem item) {
                switch (helper.getItemViewType()) {
                    case MultiItem.SEND:
                        helper.setText(R.id.chat_from_content, item.content);
                        //helper.setImageBitmap(R.id.chat_from_icon,getRoundCornerBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.chat_head), 16));
                        break;
                    case MultiItem.FROM:
                        helper.setText(R.id.chat_send_content, item.content);
                        //helper.setImageBitmap(R.id.chat_send_icon,getRoundCornerBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.from_head), 16));
                        break;
                }
            }

            @Override
            protected void addItemLayout() {
                addItemType(MultiItem.SEND, R.layout.chat_send_msg);
                addItemType(MultiItem.FROM, R.layout.chat_from_msg);
            }
        });

        mAdapter.openLoadAnimation(true);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiItem multiItem = new MultiItem();
                multiItem.itemType = MultiItem.SEND;
                multiItem.content = etChat.getText().toString();
                mAdapter.add(multiItem);

                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                //mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }


    public static List<MultiItem> getMultiItemDatas() {
        List<MultiItem> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            MultiItem multiItem = new MultiItem();
            if (i % 2 == 0) {
                multiItem.itemType = MultiItem.SEND;
                multiItem.content = "海，妹子约吗";
            } else {
                multiItem.itemType = MultiItem.FROM;
                multiItem.content = "大哥，你别怕";
            }
            list.add(multiItem);
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
