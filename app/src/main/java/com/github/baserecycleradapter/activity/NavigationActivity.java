package com.github.baserecycleradapter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.baserecycleradapter.R;
import com.github.baserecycleradapter.comparator.PinyinComparator;
import com.github.baserecycleradapter.entity.City;
import com.github.baserecycleradapter.utils.StringUtils;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jms on 2016/8/10.
 */
public class NavigationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private TextView mHeader;

    private BaseRecyclerAdapter<City> mAdapter;

    private List<City> mCityList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mHeader = (TextView) findViewById(R.id.tv_char);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter = new BaseRecyclerAdapter<City>(this, mCityList = getDatas(), R.layout.navigation_item) {
            @Override
            protected void convert(BaseViewHolder helper, final City item) {

                if (helper.getAdapterPosition() == 0) {
                    helper.setVisible(R.id.tv_char, true);
                } else {
                    if (item.firstPinYin.equals(mCityList.get(helper.getAdapterPosition() - 1).firstPinYin)) {
                        helper.setVisible(R.id.tv_char, false);
                    } else {
                        helper.setVisible(R.id.tv_char, true);
                    }
                }

                helper.setText(R.id.tv_city, item.cityName);
                helper.setText(R.id.tv_char, item.firstPinYin);

                helper.setOnClickListener(R.id.tv_city, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "" + item.cityName, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View transView = recyclerView.findChildViewUnder(mHeader.getMeasuredWidth() / 2, mHeader.getMeasuredHeight() + 1);
                if (transView != null ) {
                    TextView tvChar = (TextView) transView.findViewById(R.id.tv_char);
                    mHeader.setText(tvChar.getText());
                }
            }
        });

    }


    public List<City> getDatas() {
        List<City> mCityList = new ArrayList<>();
        mCityList.add(getCity("北京"));
        mCityList.add(getCity("上海"));
        mCityList.add(getCity("广州"));
        mCityList.add(getCity("成都"));
        mCityList.add(getCity("大丰"));
        mCityList.add(getCity("大连"));
        mCityList.add(getCity("合肥"));
        mCityList.add(getCity("黄山"));
        mCityList.add(getCity("安阳"));
        mCityList.add(getCity("天津"));
        mCityList.add(getCity("深圳"));
        mCityList.add(getCity("广安"));
        mCityList.add(getCity("邻水"));

        Collections.sort(mCityList, new PinyinComparator());

        return mCityList;
    }

    public City getCity(String cityName) {
        City city = new City();
        city.cityName = cityName;
        city.cityPinYin = StringUtils.transformPinYin(city.cityName);
        city.firstPinYin = city.cityPinYin.substring(0, 1);
        return city;
    }
}
