package com.todo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * json解析
 */
public class GsonUtil {
    private static Gson gson;

    /**
     * 将json字符串转为 java对象
     */
    synchronized public static <T> T parse(String json, Class<T> classOfT) {
        return getGsonInstance(false).fromJson(json, classOfT);
    }

    /**
     * 将json字符串转为 java列表对象
     */
    synchronized public static <T> List<T> parse(String json, Type type) {
        return getGsonInstance(false).fromJson(json, type);
    }

    /**
     * 将obj对象转为json格式数据
     */
    synchronized public static String toJson(Object obj) {
        return getGsonInstance(false).toJson(obj);
    }

    /**
     * 获取gson实例
     */
    public static final Gson getGsonInstance(boolean isExplain) {
        if (!isExplain) {
            gson = new Gson();
        } else {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                    .create();
        }
        return gson;
    }

    /**
     * 获取gson实例(排除了FINAL、TRANSIENT、STATIC)
     */
    public static final Gson getGsonInstance() {
        gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).serializeNulls().create();
        return gson;
    }

    /**
     * 将json格式的字符串解析成Map对象 <li>
     * json格式：{"name":"admin","retries":"3fff","testname"
     * :"ddd","testretries":"fffffffff"}
     */
    public static HashMap<String, String> toHashMap(String object) {
        HashMap<String, String> data = new HashMap<String, String>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(object);
            Iterator it = jsonObject.keys();
            // 遍历jsonObject数据，添加到Map对象
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                String value = (String) jsonObject.get(key);
                data.put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


}
