package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.ScoreComponent;

import java.io.IOException;

public class ScoreComponentTypeAdapter extends TypeAdapter<ScoreComponent> {
    private Gson gson;

    public ScoreComponentTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, ScoreComponent value) throws IOException {
        out.beginObject();
        out.name("name").value(value.getName());
        out.name("objective").value(value.getObjective());
        out.name("value").value(value.getValue());
        TextComponentTypeAdapter.writeFormatting(gson, out, value);
        out.endObject();
    }

    @Override
    public ScoreComponent read(JsonReader in) throws IOException {
        ScoreComponent component = new ScoreComponent("", "");
        while (in.hasNext()) {
            if (in.peek() == JsonToken.NAME) {
                String nameString = in.nextName();
                switch (nameString) {
                    case "name":
                        component.setName(in.nextString());
                        break;
                    case "objective":
                        component.setObjective(in.nextString());
                        break;
                    case "value":
                        component.setValue(in.nextString());
                        break;
                    default:
                        TextComponentTypeAdapter.readFormatting(gson, nameString, in, component);
                }
            }
        }
        return component;
    }
}
