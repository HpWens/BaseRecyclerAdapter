package com.github.baserecycleradapter.comparator;

import com.github.baserecycleradapter.entity.City;

import java.util.Comparator;

/**
 * Created by jms on 2016/8/10.
 */
public class PinyinComparator implements Comparator<City> {
    @Override
    public int compare(City lhs, City rhs) {
        return lhs.cityPinYin.compareTo(rhs.cityPinYin);
    }
}
