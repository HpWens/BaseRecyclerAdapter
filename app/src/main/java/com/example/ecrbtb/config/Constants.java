package com.example.ecrbtb.config;

/**
 * Created by boby on 2016/12/9.
 */

public class Constants {

    //baseurl
    public static final String BASE_URL = "http://test.366ec.net";
    //public static final String BASE_URL = "http://192.168.4.67:8081";

    public static final String PAY_BASE_URL = "http://m.test.366ec.net/";

    //数据库名称
    public static final String DB_NAME = "b2b.db";
    //数据库版本
    public static final int DB_VERSION = 6;

    // SharedPreference 名称
    public static final String SHARED_PREFERENCE = "b2b_preference";

    //登录URL
    public static final String LOGIN_URL = "Route.axd?method=vast.store.manager.login&format=Json";

    //获取category
    public static final String CATEGORY_URL = "Route.axd?method=vast.mall.category.list&format=Json";

    //获取 product
    public static final String PRODUCT_URL = "Route.axd?method=vast.mall.product.listinpage&format=Json";

    //goods
    public static final String GOODS_URL = "Route.axd?method=vast.mall.specification.get&format=Json";

    //Detail
    public static final String DETAIL_URL = "Route.axd?method=vast.mall.product.get&format=Json";

    //Product like
    public static final String LIKE_URL = "Route.axd?method=vast.mall.product.listbyrandom&format=Json";

    //商品参数数据
    public static final String PARAMS_URL = "Route.axd?method=vast.mall.product.relate&format=Json";

    //提交订单
    public static final String COMMIT_ORDER_URL = "Route.axd?method=vast.Order.trade.calc&format=Json";

    //长购订单URL
    public static final String PURCHASE_URL = "Route.axd?method=vast.order.trade.list&format=Json";

    //收藏商品
    public static final String COLLECTION_PRODUCT_URL = "Route.axd?method=vast.mall.product.favorite&format=Json";

    //长购商品
    public static final String COMMODITY_PRODUCT_URL = "Route.axd?method=vast.order.trade.oftenbuyproducts&format=Json";

    //长购订单点击详情
    public static final String PURCHASE_DETAIL_URL = "WebStore/Supplier/orderInfo.aspx?oddNumber=";

    //获取默认地址ID
    public static final String GOODS_RECEIPT_URL = "Route.axd?method=vast.store.address.get&format=Json";

    //获取地址列表数据
    public static final String ADDRESS_LIST_URL = "Route.axd?method=vast.store.address.list&format=Json";

    //新增地址
    public static final String ADD_ADDRESS_URL = "Route.axd?method=vast.store.address.add&format=Json";

    //查询运费
    public static final String FREIGHT_URL = "Route.axd?method=vast.order.freight.get&format=Json";

    //提交订单
    public static final String ORDER_URL = "Route.axd?method=vast.Order.trade.submits&format=Json";

    //账期支付
    public static final String ACCOUNT_PAY = "Pay/Cashier.aspx?data=";

    //收藏
    public static final String COLLECTION_URL = "Route.axd?method=vast.mall.favorite.add&format=Json";

    //取消收藏
    public static final String CANCEL_COLLECTION_URL = "Route.axd?method=vast.mall.favorite.del&format=Json";

    //购物车
    public static final String STORE_CART_URL = "Trade/Store/Cart.aspx";

    //购买类型
    public static final String BUY_TYPE_CART = "Cart";

    public static final String BUY_TYPE_PRODUCT = "Product";

    //首页url
    public static final String HOME_URL = "Default2b.aspx";

    //请求成功的状态码
    public static final int REQUEST_SUCCESS_CODE = 1;

    //请求成功的状态
    public static final String REQUEST_SUCCESS_MSG = "获取数据成功";

    //请求失败的状态码
    public static final int REQUEST_FAILED_CODE = 0;

    //请求到的数据为空状态码
    public static final int REQUEST_EMPTY_CODE = 2;

    //请求到的数据为空状态
    public static final String REQUEST_EMPTY_MSG = "数据为空";

    //存在栏目数据
    public static final String EXIST_CATEGORY_DATA = "exist_category_data";

    //0 不存在
    public static final int UN_LOAD_CATEGORY = 0;

    //1存在
    public static final int LOADED_CATEGORY = 1;

    //门店ID
    public static final String STORE_ID = "store_id";


    //获取管理员ID
    public static final String MANAGER_ID = "manager_id";


    //读写文件权限
    public static final int FILE_PERMISSION = 0;


    //搜索商品 文件
    public static final String SEARCH_PRODUCT = "search_product";


    //koken
    public static final String TOKEN = "token";


    //现金
    public static final int SALE_MODE_PRICE = 0;

    //积分
    public static final int SALE_MODE_INTEGRAL = 1;

    //现金+积分  组合
    public static final int SALE_MODE_COMPOSE = 2;


    //返回成功码
    public static final int RESULT_SUCCESS_CODE = 1;


    //返回失败码
    public static final int RESULT_FAILED_CODE = 0;


    //adapter 索引
    public static final String ADAPTER_POSITION = "adapter_position";

    //购物车数量
    public static final String SHOPPING_CART_NUM = "cart_num";

    //商户web地址
    public static final String MERCHANT_URL = "merchant_url";

    //线上支付
    public static final String ONLINE_PAY = "/OrderSuccess.aspx?OddNumber=";

    //商品数据
    public static final String PRODUCT_DATA = "product_data";

    //商品索引位置
    public static final String PRODUCT_POSITION = "product_position";
}
