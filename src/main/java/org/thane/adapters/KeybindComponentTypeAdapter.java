package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.KeybindComponent;

import java.io.IOException;

public class KeybindComponentTypeAdapter extends TypeAdapter<KeybindComponent> {
    private Gson gson;

    public KeybindComponentTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, KeybindComponent value) throws IOException {
        out.beginObject();
        out.name("keybind").value(value.getKeybind());
        TextComponentTypeAdapter.writeFormatting(gson, out, value);
        out.endObject();
    }

    @Override
    public KeybindComponent read(JsonReader in) throws IOException {
        KeybindComponent component = new KeybindComponent();
        in.beginObject();
        while (in.hasNext()) {
            if (in.peek() == JsonToken.NAME) {
                String name = in.nextName();
                switch (name) {
                    case "keybind":
                        component.setKeybind(in.nextString());
                    default:
                        TextComponentTypeAdapter.readFormatting(gson, name, in, component);
                }
            }
        }
        in.endObject();
        return component;
    }
}
