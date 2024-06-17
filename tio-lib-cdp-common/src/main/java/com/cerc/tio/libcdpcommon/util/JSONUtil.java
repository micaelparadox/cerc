package com.cerc.tio.libcdpcommon.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONUtil {
    public static boolean isValidJson(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException e) {
            try {
                new JSONArray(json);
            } catch (JSONException e2) {
                return false;
            }
        }
        return true;
    }
}
