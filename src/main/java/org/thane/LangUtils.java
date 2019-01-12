package org.thane;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.gson.*;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.thane.adapters.ComponentTypeAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class LangUtils extends JavaPlugin {

    public static final String US_ENGLISH = "en_us";

    private static Gson GSON;

    private static final Map<Player, String> PLAYER_LOCALES = new HashMap<>();

    private static final Map<NamespacedKey, LocaleMessage> MESSAGES = new HashMap<>();

    public static LangUtils INSTANCE;

    public LangUtils() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (getServer().getPluginManager().isPluginEnabled("NMSUtils")) {
            NMSUtils.getBuilder().registerTypeAdapterFactory(new ComponentTypeAdapterFactory());
            NMSUtils.instantiateGson();
            GSON = NMSUtils.getGson();
        } else GSON = new GsonBuilder().registerTypeAdapterFactory(new ComponentTypeAdapterFactory()).create();

        settingsAdapter = new PacketAdapter(INSTANCE, PacketType.Play.Client.SETTINGS) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                event.getPacket().getStrings().read(0);
                PLAYER_LOCALES.put(event.getPlayer(), event.getPacket().getStrings().read(0));
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(settingsAdapter);

        registerLanguageFile(this, new File(this.getDataFolder().getAbsolutePath() + File.separatorChar + "test.json"));
    }

    private static PacketAdapter settingsAdapter;

    @Override
    public void onDisable() {
        ProtocolLibrary.getProtocolManager().removePacketListener(settingsAdapter);
    }

    public static void registerLanguageFile(Plugin plugin, File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(f -> registerLanguageFile0(plugin, f));
        } else registerLanguageFile0(plugin, file);
    }

    private static void registerLanguageFile0(Plugin plugin, File file) {
        if (file.getName().endsWith(".json") || file.getName().endsWith(".lang")) {
            NamespacedKey key = new NamespacedKey(plugin, file.getName().split("\\.")[0]);
            LocaleMessage message = null;
            try {
                message = GSON.fromJson(new String(Files.readAllBytes(file.toPath())), LocaleMessage.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message != null) MESSAGES.put(key, message);
        }
    }

    public static String getPlayerLocale(Player player) {
        return PLAYER_LOCALES.get(player);
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void setMessage(NamespacedKey key, LocaleMessage message) {
        MESSAGES.put(key, message);
    }

    public static LocaleMessage getMessage(NamespacedKey key) {
        return MESSAGES.get(key);
    }

    public static void removeMessage(NamespacedKey key) {
        MESSAGES.remove(key);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("display")) {
                getMessage(new NamespacedKey(this, "test")).sendTo(LocaleMessage.createArgumentBuilder()
                        .put("%player%", ((Player) sender).getDisplayName()).asMap(), (Player) sender);
                return true;
            }
        }
        return false;
    }
}
