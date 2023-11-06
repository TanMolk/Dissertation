package uk.ac.ncl.c8099.wei.backend.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.ac.ncl.c8099.wei.backend.utils.support.LocalDateTimeAdapter;

import java.time.LocalDateTime;

/**
 * @author wei tan
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    public static String toJsonStr(Object obj) {
        String result;
        if (obj instanceof String) {
            result = (String) obj;
        } else {
            result = GSON.toJson(obj);
        }
        return result;
    }

    public static <T> T jsonStrToObj(String jsonStr, Class<T> tClass) {
        return GSON.fromJson(jsonStr, tClass);
    }
}
