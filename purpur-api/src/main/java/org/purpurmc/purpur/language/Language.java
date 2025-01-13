package org.purpurmc.purpur.language;

import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a language that can translate translation keys
 */
@NullMarked
public abstract class Language {
    private static @Nullable Language language;

    /**
     * Returns the default language of the server
     */
    @Nullable
    public static Language getLanguage() {
        return language;
    }

    public static void setLanguage(Language language) {
        if (Language.language != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Language");
        }
        Language.language = language;
    }

    /**
     * Checks if a certain translation key is translatable with this language
     * @param key The translation key
     * @return Whether this language can translate the key
     */
    abstract public boolean has(String key);

    /**
     * Checks if a certain translation key is translatable with this language
     * @param key The translation key
     * @return Whether this language can translate the key
     */
    public boolean has(Translatable key) {
        return has(key.translationKey());
    }

    /**
     * Translates a translation key to this language
     * @param key The translation key
     * @return The translated key, or the translation key if it couldn't be translated
     */
    abstract public String getOrDefault(String key);

    /**
     * Translates a translation key to this language
     * @param key The translation key
     * @return The translated key, or the translation key if it couldn't be translated
     */
    public String getOrDefault(Translatable key) {
        return getOrDefault(key.translationKey());
    }
}
