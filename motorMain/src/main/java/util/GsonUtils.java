package util;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by padmate on 2018/3/8.
 */

public class GsonUtils {

    public static <T> T parseJSON(String json, Class<T> clazz) {
        Gson gson = new Gson();
        boolean failed = false;
        T info = null;
        try {
            info = gson.fromJson(json, clazz);
        } catch (IllegalStateException | JsonSyntaxException exception) {
            failed = true;
        }
        if (failed) {
            System.out.println("parseJSON "+"IllegalStateException | JsonSyntaxException");
        }
        return info;
    }

    /**
     * Type type = new
     TypeToken&lt;ArrayList&lt;TypeInfo>>(){}.getType();
     Type所在的包：java.lang.reflect
     TypeToken所在的包：com.google.gson.reflect.TypeToken
     * @param
     * @param type
     * @return
     */
    public static <T> T parseJson(String jsonArr, Type type){
        Gson gson = new Gson();
        T info = gson.fromJson(jsonArr, type);
        return info;
    }
}
