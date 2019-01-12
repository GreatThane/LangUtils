package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.TranslatableComponent;

import java.io.IOException;
import java.util.List;

public class TranslatableComponentTypeAdapter extends TypeAdapter<TranslatableComponent> {
    private Gson gson;

    public TranslatableComponentTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, TranslatableComponent value) throws IOException {
        out.beginObject();
        out.name("message node").value(value.getTranslate());
        if (value.getWith() != null) {
            out.name("with");
            gson.getAdapter(List.class).write(out, value.getWith());
            TextComponentTypeAdapter.writeFormatting(gson, out, value);
        }
        out.endObject();
    }

    @Override
    public TranslatableComponent read(JsonReader in) throws IOException {
        TranslatableComponent component = new TranslatableComponent();
        while (in.hasNext()) {
            if (in.peek() == JsonToken.NAME) {
                String name = in.nextName();
                switch (name) {
                    case "message node":
                    case "translate":
                    case "node":
                        component.setTranslate(in.nextString());
                        break;
                    case "with":
                        component.setWith(gson.getAdapter(List.class).read(in));
                        break;
                    default:
                        TextComponentTypeAdapter.readFormatting(gson, name, in, component);
                }
            }
        }
        return null;
    }
}
