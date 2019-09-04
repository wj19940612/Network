package com.hyx.network.converter.gsontypeadapter;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * created by ${nishuideyu}
 * 2019/8/8
 */
public class LongTypeAdapter extends TypeAdapter<Long> {
    @Override
    public void write(JsonWriter out, Long value) throws IOException {
        out.value(value);
    }

    @Override
    public Long read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return 0L;
        }
        try {
            String result = in.nextString();
            if ("".equals(result)) {
                return 0L;
            }
            return Long.parseLong(result);
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
