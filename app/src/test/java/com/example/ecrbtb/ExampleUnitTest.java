package com.example.ecrbtb;

import android.databinding.repacked.apache.commons.codec.binary.Base64;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @org.junit.Test
    public void addition_isCorrect() throws Exception {

        String agrs = "?pid=759&sid=19";

        String ss = agrs.substring(agrs.indexOf("pid=")+4, agrs.indexOf("&"));

        System.out.println(""+ss);



        String s = new String(Base64.encodeBase64(agrs.getBytes("utf-8")));

        System.out.println("!!" + s);


        String str = "http://test.366ec.net/UserFiles/EditorFiles/image/20161227/20161227094734_6292.png,";

        System.out.println("-------------------" + str.substring(0, str.length() - 1));

//        if (str.contains("src=")) {
//            while (true) {
//                str = str.substring(str.indexOf("src=") +5);
//                if (str == null || StringUtils.isEmpty(str)) return;
//                if (!str.startsWith("http://")) {
//                    return;
//                } else {
//                    String url = str.substring(0, str.indexOf("\""));
//                    System.out.println("************" + url);
//                }
//            }
//        }


//        List<String> priceList = new ArrayList<String>();
//
//        priceList.add("¥10.0+12.0积分");
//        priceList.add("15.0积分");
//        priceList.add("¥219.0815");
//
//        double price = 0;
//        double integral = 0;
//        double deduction = 0;
//        String deductionStr = "";
//        //"¥"
//        for (String str : priceList) {
//            //¥+积分
//            if (str.startsWith("¥")) {
//                if (str.contains("+")) {
//                    price += Double.parseDouble(str.substring(str.indexOf("¥") + 1, str.indexOf("+")));
//                    integral += Double.parseDouble(str.substring(str.indexOf("+") + 1, str.indexOf("积")));
//                } else if (str.contains("可抵")) {
//                    deductionStr += str;
//                } else {
//                    price += Double.parseDouble(str.substring(str.indexOf("¥") + 1, str.length()));
//                }
//            } else {
//                integral += Double.parseDouble(str.substring(0, str.indexOf("积")));
//            }
//        }
//
//
//        String testStr = "¥219.0815可抵10.0积分";
//
//
//        String priceS = testStr.substring(testStr.indexOf("¥") + 1, testStr.indexOf("可"));
//
//        String integralS=testStr.substring(testStr.indexOf("抵") + 1, testStr.indexOf("积"));
//
//
//        System.out.println("********************" + priceS);
//
//        System.out.println("********************" + integralS);


        // String resultPrice = "¥" + price + " + " + integral + "积分";


        //System.out.println("********************" + resultPrice);

//        if (mIsShowSearchData) {
//            mPresenter.requestProductData(storeId, storeFKId, 0, mCurrentPage, false, mSearchKey, true);
//        } else {
//            mPresenter.requestProductData(storeId, storeFKId, mCategoryId, mCurrentPage, false);
//        }


//        if (mIsShowSearchData) {
//            mPresenter.requestProductData(storeId, storeFKId, 0, mCurrentPage, true, mSearchKey, true);
//        } else {
//            mPresenter.requestProductData(storeId, storeFKId, mCategoryId, mCurrentPage, true);
//        }


        //assertEquals(4, 2 + 2);

        //       Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        //System.out.println(""+child.getId()+"---"+child.getName());

        //       String str = "{\"ErrCode:\":\"00001\",\"ErrMsg\":\"代码无法匹配\"}";

//        JSONObject jsonObject = new JSONObject(str);
//
//        System.out.println("" + jsonObject.has("ErrCode"));

//        List<Product> list = gson.fromJson("[]", new TypeToken<List<Product>>() {
//        }.getType());
//
//        System.out.println(""+list.isEmpty());

    }
}