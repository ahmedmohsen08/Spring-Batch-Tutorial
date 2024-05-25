package com.basata.billnotifications.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import javax.json.*;

@Slf4j
@Component
public class ResponseService {

    public Object getKeyFromObject(String object, String key) {
        JsonObject rootObject;
        try {
            JsonReader reader = Json.createReader(new StringReader(object));
            rootObject = reader.readObject();
            reader.close();

            JsonArray data = rootObject.getJsonArray("data");
            JsonObject firstObject = data.getJsonObject(0);
            JsonObject result = firstObject.getJsonObject(key);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    public Double getKeyDoubleFromObject(String object, String key) {
        return Double.valueOf((String) getKeyFromObject(object, key));
    }
}
