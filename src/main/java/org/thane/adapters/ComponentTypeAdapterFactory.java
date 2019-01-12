package org.thane.adapters;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.thane.LocaleMessage;

public class ComponentTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> clazz = type.getRawType();

        if (TextComponent.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new TextComponentTypeAdapter(gson);
        } else if (KeybindComponent.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new KeybindComponentTypeAdapter(gson);
        } else if (ScoreComponent.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new ScoreComponentTypeAdapter(gson);
        } else if (SelectorComponent.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new SelectorComponentTypeAdapter(gson);
        } else if (TranslatableComponent.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new TranslatableComponentTypeAdapter(gson);
        } else if (BaseComponent.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new BaseComponentTypeAdapter(gson);
        } else if (LocaleMessage.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new LocaleMessageTypeAdapter(gson);
        } else if (ChatColor.class.isAssignableFrom(clazz)) {
            return (TypeAdapter<T>) new ChatColorTypeAdapter();
        } else return null;
    }
}
