package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;
import java.util.List;

public class TextComponentTypeAdapter extends TypeAdapter<TextComponent> {
    private Gson gson;

    public TextComponentTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, TextComponent value) throws IOException {
        out.beginObject();
        out.name("text").value(value.getText());
        writeFormatting(gson, out, value);
        out.endObject();
    }

    @Override
    public TextComponent read(JsonReader in) throws IOException {
        TextComponent text = new TextComponent();
        in.beginObject();
        while (in.hasNext()) {
            if (in.peek() == JsonToken.NAME) {
                String name = in.nextName();
                switch (name) {
                    case "text":
                        text.setText(in.nextString());
                        break;
                    default:
                        readFormatting(gson, name, in, text);
                }
            }
        }
        in.endObject();
        return text;
    }

    public static void writeFormatting(Gson gson, JsonWriter out, BaseComponent value) throws IOException {
        if (value.getColor() != null && value.getColor() != ChatColor.RESET) {
            out.name("color");
            gson.getAdapter(ChatColor.class).write(out, value.getColor());
        }
        if (value.isBold()) {
            out.name("bold").value(value.isBold());
        }
        if (value.isItalic()) {
            out.name("italic").value(value.isItalic());
        }
        if (value.isObfuscated()) {
            out.name("obfuscated").value(value.isObfuscated());
        }
        if (value.isStrikethrough()) {
            out.name("strikethrough").value(value.isStrikethrough());
        }
        if (value.isUnderlined()) {
            out.name("underlined").value(value.isUnderlined());
        }
        if (value.getInsertion() != null && !value.getInsertion().isEmpty()) {
            out.name("insertion").value(value.getInsertion());
        }
        if (value.getClickEvent() != null) {
            out.name("click event");
            gson.getAdapter(ClickEvent.class).write(out, value.getClickEvent());
        }
        if (value.getHoverEvent() != null) {
            out.name("hover event");
            gson.getAdapter(HoverEvent.class).write(out, value.getHoverEvent());
        }
        if (value.getExtra() != null) {
            out.name("extra");
            gson.getAdapter(List.class).write(out, value.getExtra());
        }
    }

    public static void readFormatting(Gson gson, String name, JsonReader in, BaseComponent value) throws IOException {
        switch (name) {
            case "color":
                ChatColor color = gson.getAdapter(ChatColor.class).read(in);
                value.setColor(color);
                break;
            case "bold":
                value.setBold(in.nextBoolean());
                break;
            case "italic":
            case "italicize":
                value.setItalic(in.nextBoolean());
                break;
            case "obfuscated":
            case "obfuscate":
                value.setObfuscated(in.nextBoolean());
                break;
            case "strikethrough":
            case "strike through":
            case "strike-through":
            case "strike":
                value.setStrikethrough(in.nextBoolean());
                break;
            case "underlined":
            case "underline":
                value.setUnderlined(in.nextBoolean());
                break;
            case "insertion":
                value.setInsertion(in.nextString());
                break;
            case "click event":
            case "click":
                value.setClickEvent(gson.getAdapter(ClickEvent.class).read(in));
                break;
            case "hover event":
            case "hover":
                value.setHoverEvent(gson.getAdapter(HoverEvent.class).read(in));
                break;
            case "extra":
                value.setExtra(gson.getAdapter(List.class).read(in));
                break;
        }
    }
}
