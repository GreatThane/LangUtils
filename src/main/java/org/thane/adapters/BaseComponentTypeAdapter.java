package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.chat.*;

import java.io.IOException;
import java.util.List;

public class BaseComponentTypeAdapter extends TypeAdapter<BaseComponent> {
    private Gson gson;

    public BaseComponentTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, BaseComponent value) throws IOException {
        out.beginObject();
        if (value instanceof TextComponent) {
            out.name("text").value(((TextComponent) value).getText());
        } else if (value instanceof KeybindComponent) {
            out.name("keybind").value(((KeybindComponent) value).getKeybind());
        } else if (value instanceof ScoreComponent) {
            out.name("name").value(((ScoreComponent) value).getName());
            out.name("objective").value(((ScoreComponent) value).getObjective());
            out.name("value").value(((ScoreComponent) value).getValue());
        } else if (value instanceof SelectorComponent) {
            out.name("selector").value(((SelectorComponent) value).getSelector());
        } else if (value instanceof TranslatableComponent) {
            out.name("message node").value(((TranslatableComponent) value).getTranslate());
            if (((TranslatableComponent) value).getWith() != null) {
                out.name("with");
                gson.getAdapter(List.class).write(out, ((TranslatableComponent) value).getWith());
                TextComponentTypeAdapter.writeFormatting(gson, out, value);
            }
        }
        TextComponentTypeAdapter.writeFormatting(gson, out, value);
        out.endObject();
    }

    @Override
    public BaseComponent read(JsonReader in) throws IOException {
        BaseComponent component = new TextComponent();
        if (in.peek() == JsonToken.BEGIN_OBJECT) {
            in.beginObject();
            while (in.hasNext()) {
                if (in.peek() == JsonToken.NAME) {
                    String name = in.nextName();
                    switch (name) {
                        case "text":
                            if (!(component instanceof TextComponent)) component = new TextComponent();
                            ((TextComponent) component).setText(in.nextString());
                            break;
                        case "keybind":
                            if (!(component instanceof KeybindComponent)) component = new KeybindComponent();
                            ((KeybindComponent) component).setKeybind(in.nextString());
                            break;
                        case "name":
                            if (!(component instanceof ScoreComponent)) component = new ScoreComponent("", "");
                            ((ScoreComponent) component).setName(in.nextString());
                            break;
                        case "objective":
                            if (!(component instanceof ScoreComponent)) component = new ScoreComponent("", "");
                            ((ScoreComponent) component).setObjective(in.nextString());
                            break;
                        case "value":
                            if (!(component instanceof ScoreComponent)) component = new ScoreComponent("", "");
                            ((ScoreComponent) component).setValue(in.nextString());
                            break;
                        case "selector":
                            if (!(component instanceof SelectorComponent)) component = new SelectorComponent("");
                            ((SelectorComponent) component).setSelector(in.nextString());
                            break;
                        case "message node":
                        case "translate":
                        case "node":
                            if (!(component instanceof TranslatableComponent)) component = new TranslatableComponent();
                            ((TranslatableComponent) component).setTranslate(in.nextString());
                            break;
                        case "with":
                            if (!(component instanceof TranslatableComponent)) component = new TranslatableComponent();
                            ((TranslatableComponent) component).setWith(gson.getAdapter(List.class).read(in));
                            break;
                        default:
                            TextComponentTypeAdapter.readFormatting(gson, name, in, component);
                    }
                }
            }
            in.endObject();
        } else ((TextComponent) component).setText(in.nextString());
        return component;
    }
}
