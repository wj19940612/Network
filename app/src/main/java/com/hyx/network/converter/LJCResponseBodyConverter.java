package com.hyx.network.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class LJCResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = LJCResponseBodyConverter.class.getSimpleName();
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    LJCResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());

        try {
            T result = adapter.read(jsonReader);

            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                String error = "JSON document was not fully consumed.";
                Log.e(TAG, error);
                throw new JsonIOException(error);
            }

            return result;
        } catch (Exception e) {
            Log.e(TAG, "Parse JSON error." + e.toString());
            throw new JsonParseException(e);
        } finally {
            value.close();
        }
    }
}