package org.thane.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.io.IOException;

public class ChatColorTypeAdapter extends TypeAdapter<ChatColor> {

    @Override
    public void write(JsonWriter out, ChatColor value) throws IOException {
        out.value(value.getName());
        Bukkit.getLogger().info("writing chat color!");
    }

    @Override
    public ChatColor read(JsonReader in) throws IOException {
        ChatColor color;
        String value = in.nextString();
        try {
            color = ChatColor.valueOf(value.toUpperCase().trim().replaceAll("\\s+", "_"));
        } catch (IllegalArgumentException e) {
            color = ChatColor.getByChar(value.trim().replace("&", "").replace(String.valueOf(ChatColor.COLOR_CHAR), "").trim().charAt(0));
        }
        return color;
    }
}
