package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.thane.LocaleMessage;

import java.io.IOException;
import java.util.Map;

public class LocaleMessageTypeAdapter extends TypeAdapter<LocaleMessage> {
    private Gson gson;

    public LocaleMessageTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, LocaleMessage value) throws IOException {
        gson.getAdapter(Map.class).write(out, value.getLocales());
    }

    @Override
    public LocaleMessage read(JsonReader in) throws IOException {
        return new LocaleMessage((Map<String, BaseComponent[]>) gson.getAdapter(TypeToken.get(new TypeToken<Map<String, BaseComponent[]>>() {}.getType())).read(in));
    }
}
