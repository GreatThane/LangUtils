package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.SelectorComponent;

import java.io.IOException;

public class SelectorComponentTypeAdapter extends TypeAdapter<SelectorComponent> {
    private Gson gson;

    public SelectorComponentTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, SelectorComponent value) throws IOException {
        out.beginObject();
        out.name("selector").value(value.getSelector());
        TextComponentTypeAdapter.writeFormatting(gson, out, value);
        out.endObject();
    }

    @Override
    public SelectorComponent read(JsonReader in) throws IOException {
        SelectorComponent component = new SelectorComponent("");
        in.beginObject();
        while (in.hasNext()) {
            if (in.peek() == JsonToken.NAME) {
                String name = in.nextName();
                switch (name) {
                    case "selector":
                        component.setSelector(in.nextString());
                        break;
                    default:
                        TextComponentTypeAdapter.readFormatting(gson, name, in, component);
                }
            }
        }
        in.endObject();
        return component;
    }
}
