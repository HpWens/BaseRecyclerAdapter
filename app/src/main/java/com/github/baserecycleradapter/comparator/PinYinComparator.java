package com.github.baserecycleradapter.comparator;

import com.github.baserecycleradapter.entity.City;

import java.util.Comparator;


/**
 * Created by Administrator on 8/10 0010.
 */
public class PinYinComparator implements Comparator<City> {
    @Override
    public int compare(City city, City t1) {
        return city.cityPinYin.compareTo(t1.cityPinYin);
    }
}
