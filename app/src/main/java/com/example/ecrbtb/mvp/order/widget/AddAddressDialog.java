package com.example.ecrbtb.mvp.order.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.ecrbtb.R;
import com.example.ecrbtb.mvp.order.bean.Address;
import com.example.ecrbtb.mvp.order.bean.ProvinceBean;
import com.example.ecrbtb.mvp.order.reader.JsonFileReader;
import com.example.ecrbtb.utils.RegularUtils;
import com.example.ecrbtb.utils.StringUtils;
import com.flyco.dialog.widget.base.BottomBaseDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boby on 2017/1/5.
 */

public class AddAddressDialog extends BottomBaseDialog<ShoppingAddressDialog> {

    private Context ctx;

    private EditText etName;

    private EditText etPhone;

    private EditText etStreet;

    private Button btnCancel;

    private Button btnAdd;

    private LinearLayout linearSelectAddress;

    private TextView tvAddress;

    private OptionsPickerView pvOptions;

    private Address addressBean;

    //  省份
    ArrayList<ProvinceBean> provinceBeanList = new ArrayList<>();
    //  城市
    ArrayList<String> cities;
    ArrayList<List<String>> cityList = new ArrayList<>();
    //  区/县
    ArrayList<String> district;
    ArrayList<List<String>> districts;
    ArrayList<List<List<String>>> districtList = new ArrayList<>();


    public AddAddressDialog(Context context, View animateView) {
        super(context, animateView);
        this.ctx = context;
        this.addressBean = new Address();
        init();
    }

    private void init() {
        widthScale(1.0f);
    }


    @Override
    public View onCreateView() {
        final View inflate = View.inflate(mContext, R.layout.dialog_add_address, null);
        linearSelectAddress = (LinearLayout) inflate.findViewById(R.id.linear_select_address);
        tvAddress = (TextView) inflate.findViewById(R.id.tv_address);
        btnCancel = (Button) inflate.findViewById(R.id.btn_cancel);

        etName = (EditText) inflate.findViewById(R.id.et_name);
        etPhone = (EditText) inflate.findViewById(R.id.et_phone);
        etStreet = (EditText) inflate.findViewById(R.id.et_street);
        btnAdd = (Button) inflate.findViewById(R.id.btn_add);

        initOptions(inflate.getContext(), (ViewGroup) inflate);

        return inflate;
    }

    //添加数据
    private void initOptions(Context context, ViewGroup rootView) {

        pvOptions = new OptionsPickerView(context, rootView);

        //  获取json数据
        String province_data_json = JsonFileReader.getJson(context, "province_data.json");
        //  解析json数据
        parseJson(province_data_json);

        //三级联动效果
        pvOptions.setPicker(provinceBeanList, cityList, districtList, true);

        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, false, false);

        // 设置默认选中的三级项目
        pvOptions.setSelectOptions(0, 0, 0);
        //  监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String city = provinceBeanList.get(options1).getPickerViewText();
                String address = "";
                //  如果是直辖市或者特别行政区只设置市和区/县
                if ("北京市".equals(city) || "上海市".equals(city) || "天津市".equals(city) || "重庆市".equals(city) || "澳门".equals(city) || "香港".equals(city)) {
                    try {
                        address = provinceBeanList.get(options1).getPickerViewText()
                                + " " + districtList.get(options1).get(option2).get(options3);
                        addressBean.Province = addressBean.City = provinceBeanList.get(options1).getPickerViewText();
                        addressBean.Area = districtList.get(options1).get(option2).get(options3);
                    }catch (IndexOutOfBoundsException e){
                        address = provinceBeanList.get(0).getPickerViewText()
                                + " " + districtList.get(0).get(0).get(0);
                        addressBean.Province = addressBean.City = provinceBeanList.get(0).getPickerViewText();
                        addressBean.Area = districtList.get(0).get(0).get(0);
                    }
                } else {
                    try {
                        address = provinceBeanList.get(options1).getPickerViewText()
                                + " " + cityList.get(options1).get(option2)
                                + " " + districtList.get(options1).get(option2).get(options3);
                        addressBean.Province = provinceBeanList.get(options1).getPickerViewText();
                        addressBean.City = cityList.get(options1).get(option2);
                        addressBean.Area = districtList.get(options1).get(option2).get(options3);
                    } catch (IndexOutOfBoundsException e) {
                        address = provinceBeanList.get(0).getPickerViewText()
                                + " " + cityList.get(0).get(0)
                                + " " + districtList.get(0).get(0).get(0);
                        addressBean.Province = provinceBeanList.get(0).getPickerViewText();
                        addressBean.City = cityList.get(0).get(0);
                        addressBean.Area = districtList.get(0).get(0).get(0);
                    }
                }
                tvAddress.setText(address);
            }
        });
    }

    @Override
    public void setUiBeforShow() {
        linearSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String address = tvAddress.getText().toString();
                String street = etStreet.getText().toString();

                RegularUtils regularUtils = RegularUtils.getRegular();

                if (StringUtils.isEmpty(name)) {
                    etName.setError("收货人姓名不能为空");
                    Toast.makeText(ctx, "收货人姓名不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(phone)) {
                    etPhone.setError("收货人电话不能为空");
                    Toast.makeText(ctx, "收货人电话不能为空", Toast.LENGTH_SHORT).show();
                } else if (!regularUtils.isMatches(regularUtils.PatCellPhoneNum, phone)) {
                    etPhone.setError("手机号码格式不对");
                    Toast.makeText(ctx, "手机号码格式不对", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(street)) {
                    etStreet.setError("详细地址不能为空");
                    Toast.makeText(ctx, "详细地址不能为空", Toast.LENGTH_SHORT).show();
                } else if (StringUtils.isEmpty(address)) {
                    Toast.makeText(ctx, "请选择地区", Toast.LENGTH_SHORT).show();
                } else {

                    addressBean.Name = name;
                    addressBean.Mobile = phone;
                    addressBean.Address = street;

                    if (listener != null) {
                        listener.onIAddAddress(addressBean);
                    }

                    dismiss();
                }
            }
        });
    }

    //  解析json填充集合
    public void parseJson(String str) {
        try {
            //  获取json中的数组
            JSONArray jsonArray = new JSONArray(str);
            //  遍历数据组
            for (int i = 0; i < jsonArray.length(); i++) {
                //  获取省份的对象
                JSONObject provinceObject = jsonArray.optJSONObject(i);
                //  获取省份名称放入集合
                String provinceName = provinceObject.getString("name");
                Log.e("AddAddressDialog", "parseJson--------" + provinceName);
                provinceBeanList.add(new ProvinceBean(provinceName));
                //  获取城市数组
                JSONArray cityArray = provinceObject.optJSONArray("city");
                cities = new ArrayList<>();//   声明存放城市的集合
                districts = new ArrayList<>();//声明存放区县集合的集合
                //  遍历城市数组
                for (int j = 0; j < cityArray.length(); j++) {
                    //  获取城市对象
                    JSONObject cityObject = cityArray.optJSONObject(j);
                    //  将城市放入集合
                    String cityName = cityObject.optString("name");
                    cities.add(cityName);
                    district = new ArrayList<>();// 声明存放区县的集合
                    //  获取区县的数组
                    JSONArray areaArray = cityObject.optJSONArray("area");
                    //  遍历区县数组，获取到区县名称并放入集合
                    for (int k = 0; k < areaArray.length(); k++) {
                        String areaName = areaArray.getString(k);
                        district.add(areaName);
                    }
                    //  将区县的集合放入集合
                    districts.add(district);
                }
                //  将存放区县集合的集合放入集合
                districtList.add(districts);
                //  将存放城市的集合放入集合
                cityList.add(cities);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private IAddAddress listener;

    public interface IAddAddress {
        public void onIAddAddress(Address address);
    }

    public void setOnAddAddress(IAddAddress iAddAddress) {
        this.listener = iAddAddress;
    }
}
