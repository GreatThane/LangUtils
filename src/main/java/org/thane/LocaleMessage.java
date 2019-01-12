package org.thane;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocaleMessage {

    private Map<String, BaseComponent[]> locales = new HashMap<>();

    public LocaleMessage() {
    }

    public LocaleMessage(Map<String, BaseComponent[]> locales) {
        this.locales = locales;
    }

    public Map<String, BaseComponent[]> getLocales() {
        return locales;
    }

    public void setLocales(Map<String, BaseComponent[]> locales) {
        this.locales = locales;
    }

    public void putLocale(String locale, BaseComponent[] components) {
        locales.put(locale, components);
    }

    public void removeLocale(String locale) {
        locales.remove(locale);
    }

    public BaseComponent[] getMessage(String locale) {
        BaseComponent[] message = locales.get(locale);
        if (message == null) {
            String language = locale.substring(0, locale.indexOf("_") + 2);
            Optional<Map.Entry<String, BaseComponent[]>> entry = locales.entrySet().stream().filter(e -> e.getKey().startsWith(language)).findFirst();
            if (entry.isPresent()) {
                message = entry.get().getValue();
            }
        }
        return message;
    }

    public boolean supportsLanguage(String locale) {
        String language = locale.substring(0, locale.indexOf("_") + 2);
        return locales.entrySet().stream().anyMatch(e -> e.getKey().startsWith(language));
    }

    public boolean hasLocale(String locale) {
        return locales.containsKey(locale);
    }

    public void sendToAll() {
        sendToAll(ChatMessageType.CHAT);
    }

    public void sendToAll(Map<Pattern, String> replacementRules) {
        sendTo(ChatMessageType.CHAT, replacementRules);
    }

    public void sendToAll(ChatMessageType type) {
        sendTo(type, Bukkit.getOnlinePlayers());
    }

    public void sendToAll(ChatMessageType type, Map<Pattern, String> replacementRules) {
        sendTo(type, replacementRules, Bukkit.getOnlinePlayers());
    }

    public void sendTo(Collection<? extends Player> players) {
        sendTo(ChatMessageType.CHAT, players);
    }

    public void sendTo(Map<Pattern, String> replacementRules, Collection<? extends Player> players) {
        sendTo(ChatMessageType.CHAT, replacementRules, players);
    }

    public void sendTo(Map<Pattern, String> replacementRules, Player... players) {
        sendTo(ChatMessageType.CHAT, replacementRules, players);
    }

    public void sendTo(ChatMessageType type, Collection<? extends Player> players) {
        sendTo(type, players.toArray(new Player[0]));
    }

    public void sendTo(ChatMessageType type, Map<Pattern, String> replacementRules, Collection<? extends Player> players) {
        sendTo(type, replacementRules, players.toArray(new Player[0]));
    }

    public void sendTo(Player... players) {
        sendTo(ChatMessageType.CHAT, players);
    }

    public void sendTo(ChatMessageType type, Player... players) {
        sendTo(type, new HashMap<>(), players);
    }

    public void sendTo(ChatMessageType type, Map<Pattern, String> replacementRules, Player... players) {

        Arrays.stream(players).forEach(p -> {
            String playerLanguageSetting = LangUtils.getPlayerLocale(p);
            String language;
            if (supportsLanguage(playerLanguageSetting)) {
                if (locales.containsKey(playerLanguageSetting)) {
                    language = playerLanguageSetting;
                } else {
                    Optional<Map.Entry<String, BaseComponent[]>> optional = locales.entrySet().stream()
                            .filter(e -> e.getKey().startsWith(playerLanguageSetting.substring(0, playerLanguageSetting.indexOf("_") + 2))).findFirst();
                    if (optional.isPresent()) {
                        language = optional.get().getKey();
                    } else language = LangUtils.US_ENGLISH;
                }
            } else language = LangUtils.US_ENGLISH;
//            BaseComponent[] baseComponents = supportsLanguage(LangUtils.getPlayerLocale(p)) ? locales.get(LangUtils.getPlayerLocale(p)) : ;
            BaseComponent[] components = Arrays.stream(locales.get(language))
                    .map(BaseComponent::duplicate).map(c -> {
                        if (c instanceof TextComponent) {
                            String text = ((TextComponent) c).getText();
                            for (Map.Entry<Pattern, String> entry : replacementRules.entrySet()) {
                                Matcher matcher = entry.getKey().matcher(text);
                                if (matcher.find()) {
                                    text = text.replaceAll(matcher.group(0), entry.getValue());
                                    ((TextComponent) c).setText(text);
                                }
                            }
                            return c;
                        } else return c;
                    }).toArray(BaseComponent[]::new);

            p.spigot().sendMessage(type, components);
        });
    }

    public static ArgumentBuilder createArgumentBuilder() {
        return new ArgumentBuilder(new HashMap<>());
    }

    public String toLegacytext(Player player) {
        return toLegacyText(LangUtils.getPlayerLocale(player));
    }

    public String toLegacyText(String locale) {
        return Arrays.stream(locales.get(locale)).map(c -> c.toLegacyText()).collect(Collectors.joining());
    }

    static class ArgumentBuilder extends MapBuilder<Pattern, String> {

        ArgumentBuilder(Map<Pattern, String> map) {
            super(map);
        }

        public ArgumentBuilder put(String key, String value) {
            asMap().put(Pattern.compile(key), value);
            return this;
        }

        public ArgumentBuilder putIfAbsent(String key, String value) {
            asMap().putIfAbsent(Pattern.compile(key), value);
            return this;
        }
    }
}
