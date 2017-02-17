package com.example.ecrbtb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

    private static XmlUtils xmlx = null;

    private XmlUtils() {
    }

    // 构造对象
    public static XmlUtils getXmlx() {
        if (xmlx == null) {
            xmlx = new XmlUtils();
        }
        return xmlx;
    }

    // 获取本地InputStream
    public InputStream getInputStream_Local(Context context, String xmlName) {
        AssetManager _AssetManager = context.getAssets();
        InputStream _InputStream = null;
        try {
            // 保存在assets文件夹下
            _InputStream = _AssetManager.open(xmlName);
            _AssetManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _InputStream;
    }

    // Xml转Model
    public <Model> List<Model> getListModel_Xml(Model _Model,
                                                InputStream _InputStream) {
        List<Model> ListModel = new ArrayList<Model>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(_InputStream, "UTF-8");
            ListModel = getListModel_xml(_Model, parser, "");
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListModel;
    }

    // Xml转Model
    public <Model> List<Model> getListModel_Xml(Model _Model,
                                                InputStream _InputStream, String endName) {
        List<Model> ListModel = new ArrayList<Model>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(_InputStream, "UTF-8");
            ListModel = getListModel_xml(_Model, parser, endName);
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ListModel;
    }

    // Xml转Model
    @SuppressWarnings("unchecked")
    public <Model> List<Model> getListModel_xml(Model _Model,
                                                XmlPullParser parser, String endName) {
        List<Model> ListModel = new ArrayList<Model>();
        try {
            // 获得类
            Class<?> _Class = _Model.getClass();
            // 实体节点开始状态
            int IsStart = 0;
            // 产生第一个事件
            int event = parser.getEventType();
            // 标签元素循环
            while (event != XmlPullParser.END_DOCUMENT) {
                // 节点名
                String nodeName = "";
                // 事件判断
                switch (event) {
                    // 文档开始
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 当前标签元素开始
                    case XmlPullParser.START_TAG:
                        // 获得节点名
                        nodeName = parser.getName();
                        // 如果是此实体xml节点
                        if (IsStart == 1) {
                            try {
                                String parText = parser.nextText();
                                // 设置实体属性值
                                setValue(_Model, nodeName, parText);
                            } catch (Exception ex) {
                                // ex.printStackTrace();
                            }
                        }
                        // 判断是否为实体xml节点
                        if (nodeName.equals(_Class.getSimpleName() + endName)) {
                            // 实体节点开始
                            IsStart = 1;
                            _Model = (Model) _Class.newInstance();
                        }
                        break;
                    // 当前标签元素结束
                    case XmlPullParser.END_TAG:
                        nodeName = parser.getName();
                        if (nodeName.equals(_Class.getSimpleName() + endName)) {
                            // 实体节点结束
                            IsStart = 0;
                            // 添加一个实体
                            ListModel.add(_Model);
                        }
                        break;
                }
                // 下一个元素
                event = parser.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ListModel;
    }

    // 强制设置实体属性值
    public <Model> void setValue(Model _Model, String FieldName, String strValue)
            throws NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field _Field = _Model.getClass().getField(FieldName);
        // 值
        // String parText = parser.nextText();
        if (_Field.getType() == String.class) {
            _Field.set(_Model, strValue);
        } else if (_Field.getType() == int.class) {
            try {
                _Field.set(_Model, Integer.parseInt(strValue));
            } catch (Exception ex) {
                // 布尔值转换
                if (strValue.toLowerCase().equals("true")) {
                    _Field.set(_Model, 1);
                } else if (strValue.toLowerCase().equals("false")) {
                    _Field.set(_Model, 0);
                }
            }
        } else if (_Field.getType() == long.class) {
            _Field.set(_Model, Long.parseLong(strValue));
        } else if (_Field.getType() == float.class) {
            _Field.set(_Model, Float.parseFloat(strValue));
        } else if (_Field.getType() == double.class) {
            _Field.set(_Model, Double.parseDouble(strValue));
        } else if (_Field.getType() == boolean.class) {
            strValue = strValue.toLowerCase();
            _Field.set(_Model, Boolean.parseBoolean(strValue));
        } else if (_Field.getType() == short.class) {
            _Field.set(_Model, Short.parseShort(strValue));
        } else if (_Field.getType() == byte.class) {
            _Field.set(_Model, Byte.parseByte(strValue));
        }
    }

    // Xml转Model
    public <Model> List<Model> getListModel_Xml(Model _Model, String xmlStr) {
        List<Model> ListModel = new ArrayList<Model>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(xmlStr));
            ListModel = getListModel_xml(_Model, parser, "");
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return ListModel;
    }

    // Xml转Model
    public <Model> List<Model> getListModel_Xml(Model _Model, String xmlStr,
                                                String endName) {
        List<Model> ListModel = new ArrayList<Model>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(new StringReader(xmlStr));
            ListModel = getListModel_xml(_Model, parser, endName);
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return ListModel;
    }

    // Model转XmlString
    public <Model> String getXmlString_Model(String rootName, String tagName,
                                             Model model, List<String> listF) {
        List<Model> listM = new ArrayList<Model>();
        listM.add(model);
        return getXmlString_Model(rootName, tagName, listM, listF);
    }

    // Model转XmlString
    public <Model> String getXmlString_Model(String rootName, String tagName,
                                             List<Model> listM, List<String> listF) {
        String str = "";
        try {
            if (rootName != null) {
                str += "<" + rootName + ">";
            }
            Field[] fds = listM.get(0).getClass().getFields();
            for (Model md : listM) {
                if (tagName != null) {
                    str += "<" + tagName + ">";
                }

                for (Field fd : fds) {
                    boolean b = false;
                    String fdn = fd.getName();
                    // 是否限制字段
                    if (listF == null) {
                        b = true;
                    } else {
                        for (String sfn : listF) {
                            // 如果存在字段
                            if (sfn.equals(fdn)) {
                                b = true;
                            }
                        }
                    }
                    if (b) {
                        str += "<" + fdn + ">";
                        str += "<![CDATA[" + fd.get(md) + "]]>";
                        str += "</" + fdn + ">";
                    }
                }

                if (tagName != null) {
                    str += "</" + tagName + ">";
                }
            }
            if (rootName != null) {
                str += "</" + rootName + ">";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    // 保存Xml
    public <Model> void saveXml_Shared(Context context, String FileName,
                                       Model _Model) throws NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, NoSuchFieldException {
        SharedPreferences sp = context.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);

        Editor _Editor = sp.edit();
        Field[] _FieldA = _Model.getClass().getFields();
        for (Field _Field : _FieldA) {
            String FieldName = _Field.getName();

            // 字段值
            String FieldValue = _Field.get(_Model).toString();
            if (_Field.getType() == String.class) {
                _Editor.putString(FieldName, FieldValue);
            } else if (_Field.getType() == int.class) {
                _Editor.putInt(FieldName, Integer.parseInt(FieldValue));
            } else if (_Field.getType() == long.class) {
                _Editor.putLong(FieldName, Long.parseLong(FieldValue));
            } else if (_Field.getType() == float.class) {
                _Editor.putFloat(FieldName, Float.parseFloat(FieldValue));
            } else if (_Field.getType() == Boolean.class) {
                _Editor.putBoolean(FieldName, Boolean.parseBoolean(FieldValue));
            } else {
                _Editor.putString(FieldName, FieldValue);
            }
        }
        _Editor.commit();
    }

    // Xml转Model
    public <Model> Model getModel_SharedXml(Context context, Model _Model,
                                            String FileName) {

        SharedPreferences _Shared = context.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);

        // 获得类
        // Class<?> _Class = Class.forName(ClassName);
        // 获得类实例
        @SuppressWarnings("unchecked")
        // Model _Model = (Model) _Class.newInstance();
                Field[] _FieldA = _Model.getClass().getFields();
        for (Field _Field : _FieldA) {
            String FieldName = _Field.getName();
            try {
                // 根据字段类型赋值
                if (_Field.getType() == String.class) {
                    _Field.set(_Model, _Shared.getString(FieldName, ""));
                } else if (_Field.getType() == int.class) {
                    _Field.set(_Model, _Shared.getInt(FieldName, 0));
                } else if (_Field.getType() == long.class) {
                    _Field.set(_Model, _Shared.getLong(FieldName, 0));
                } else if (_Field.getType() == float.class) {
                    _Field.set(_Model, _Shared.getFloat(FieldName, 0));
                } else if (_Field.getType() == Boolean.class) {
                    _Field.set(_Model, _Shared.getBoolean(FieldName, false));
                } else if (_Field.getType() == double.class) {
                    _Field.set(_Model, _Shared.getFloat(FieldName, 0));
                } else if (_Field.getType() == short.class) {
                    _Field.set(_Model, _Shared.getInt(FieldName, 0));
                } else if (_Field.getType() == byte.class) {
                    _Field.set(_Model, _Shared.getString(FieldName, "0")
                            .getBytes());
                }
            } catch (Exception e) {

            }
        }
        return _Model;
    }

    // 数组转XmlString
    public String getXmlString_Arrry(String RootName, String TagName,
                                     ArrayList<String[]> XmlArray) {
        String ajax_xml = "";
        if (RootName != null) {
            ajax_xml += "<" + RootName + ">";
        }
        if (TagName != null) {
            ajax_xml += "<" + TagName + ">";
        }
        if (XmlArray != null) {
            for (int i = 0; i < XmlArray.size(); i++) {
                String[] arr = XmlArray.get(i);
                ajax_xml += "<" + arr[0] + "><![CDATA[" + arr[1] + "]]></"
                        + arr[0] + ">";
            }
        }
        if (TagName != null) {
            ajax_xml += "</" + TagName + ">";
        }
        if (RootName != null) {
            ajax_xml += "</" + RootName + ">";
        }
        return ajax_xml;
    }

    // Post基本信息xml;
    public String getXmlString_Base(String Event, String EventKey) {
        ArrayList<String[]> XmlArray = new ArrayList<String[]>();
        XmlArray.add(new String[]{"ToUserName", "YWFhYmJiY2Nj"});
        XmlArray.add(new String[]{"FromUserName", "YWJjZGVmZ2hp"});
        XmlArray.add(new String[]{"CreateTime", "0"});
        XmlArray.add(new String[]{"MsgType", "Event"});
        XmlArray.add(new String[]{"Event", Event});
        XmlArray.add(new String[]{"EventKey", EventKey});
        return getXmlString_Arrry("Api_Base", null, XmlArray);
    }

    // 数组组合 xmlPost;
    public String getxmlPost_Array(String Event, String EventKey,
                                   ArrayList<String[]> XmlArray) {
        String ajax_xml = "<xml>";
        ajax_xml += getXmlString_Base(Event, EventKey);
        ajax_xml += getXmlString_Arrry("Api_Info", null, XmlArray);
        ajax_xml += "</xml>";
        return ajax_xml;
    }

    // 数组组合 xmlPost;
    public String getxmlPost_Arraycs(String Event, String EventKey,
                                     ArrayList<String[]> XmlArray) {
        String ajax_xml = "<XmlData>";
        ajax_xml += getXmlString_Base(Event, EventKey);
        ajax_xml += getXmlString_Arrry("Api_Info", null, XmlArray);
        ajax_xml += "</XmlData>";
        return ajax_xml;
    }

    // 组合 xmlPost;
    public String getxmlPost_Xmlstr(String Event, String EventKey, String strXml) {
        String ajax_xml = "<xml>";
        ajax_xml += getXmlString_Base(Event, EventKey);
        ajax_xml += strXml;
        ajax_xml += "</xml>";
        return ajax_xml;
    }

    // 保存为SharedPreferences
    @SuppressWarnings("unchecked")
    public <Model> List<Model> getListModel_xml(Context context,
                                                String ClassName, XmlPullParser parser, String FileName) {
        SharedPreferences sp = context.getSharedPreferences(FileName,
                Context.MODE_PRIVATE);
        List<Model> ListModel = new ArrayList<Model>();
        try {
            // 获得类
            Class<?> _Class = Class.forName(ClassName);
            // 获得类实例
            Model _Model = (Model) _Class.newInstance();
            // 产生第一个事件
            int event = parser.getEventType();
            // 标签元素循环
            while (event != XmlPullParser.END_DOCUMENT) {
                // 获得节点名
                String nodeName = parser.getName();
                // 事件判断
                switch (event) {
                    // 文档开始
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 当前标签元素开始
                    case XmlPullParser.START_TAG:
                        if (nodeName.equals(_Class.getSimpleName())) {
                            _Model = (Model) _Class.newInstance();
                        }
                        try {
                            // 字段
                            Field _Field = _Class.getField(nodeName);
                            // 字段set方法
                            Method _Method = _Class.getMethod(
                                    "set" + _Field.getName(), _Field.getType());

                            Editor _Editor = sp.edit();
                            // 根据字段类型赋值
                            Object[] objA = null;
                            if (_Field.getType() == String.class) {
                                _Editor.putString(nodeName, parser.nextText());
                            } else if (_Field.getType() == int.class) {
                                _Editor.putInt(nodeName,
                                        Integer.parseInt(parser.nextText()));
                            } else if (_Field.getType() == long.class) {
                                _Editor.putLong(nodeName,
                                        Long.parseLong(parser.nextText()));
                            } else if (_Field.getType() == float.class) {
                                _Editor.putFloat(nodeName,
                                        Float.parseFloat(parser.nextText()));
                            } else if (_Field.getType() == double.class) {
                                _Editor.putString(nodeName, parser.nextText());
                            } else if (_Field.getType() == short.class) {
                                _Editor.putString(nodeName, parser.nextText());
                            } else if (_Field.getType() == byte.class) {
                                _Editor.putString(nodeName, parser.nextText());
                            } else if (_Field.getType() == Boolean.class) {
                                _Editor.putBoolean(nodeName,
                                        Boolean.parseBoolean(parser.nextText()));
                            }
                            _Editor.commit();
                        } catch (Exception ex) {

                        }
                        break;
                    // 当前标签元素结束
                    case XmlPullParser.END_TAG:
                        if (nodeName.equals(_Class.getSimpleName())) {
                            ListModel.add(_Model);
                        }
                        break;
                }
                // 下一个元素
                event = parser.next();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ListModel;
    }


    /**
     * 反射来组装xml
     *
     * @param model
     * @param <Model>
     * @return
     */
    public <Model> String getModelXml(Model model) {
        String str = "";
        Field[] fields = model.getClass().getFields();
        //加头
        str += "<" + model.getClass().getSimpleName() + ">";
        //获取节点
        for (Field field : fields) {
            try {
                String fd = field.getName();
                Object ob = field.get(model);
                //加节点头
                if (!fd.contains("$")) {
                    str += "<" + fd + ">";
                    //值
                    if (ob == null) {
                        str += "";
                    } else {
                        str += ob.toString();
                    }
                    //加节点尾
                    str += "</" + fd + ">";
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //加尾
        str += "</" + model.getClass().getSimpleName() + ">";
        return str;
    }

    /**
     * 将集合转换xml
     *
     * @param list
     * @param <Model>
     * @return
     */
    public <Model> String getListXml(List<Model> list) {
        String str = "";
        if (list != null) {
            for (Model model : list) {
                str += getModelXml(model);
            }
            return str;
        } else {
            return null;
        }
    }


    /**
     * @param model
     * @param headXml
     * @param <Model>
     * @return
     */
    public <Model> String getModelXml(Model model, String headXml) {

        String xml = "";

        // 加头
        if (!StringUtils.isEmpty(headXml)) {
            xml += "<" + headXml + ">";
        }

        //加内容
        xml += getModelXml(model);

        //加尾
        if (!StringUtils.isEmpty(headXml)) {
            xml += "</" + headXml + ">";
        }

        return xml;
    }


}
