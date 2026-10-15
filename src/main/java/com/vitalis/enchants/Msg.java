package com.vitalis.enchants;

import net.kyori.adventure.text.Component;

/** MiniMessage coloring with legacy fallback. */
public final class Msg {
    private Msg() {}

    public static Component mm(String s) {
        try {
            return net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(s);
        } catch (Exception e) {
            return Component.text(s);
        }
    }

    public static Component color(String legacy) {
        try {
            return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
                    .legacyAmpersand().deserialize(legacy);
        } catch (Exception e) {
            return Component.text(legacy);
        }
    }
}
