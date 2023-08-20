package me.xlucash.xldrop.enums;

/**
 * Enum representing various messages used throughout the plugin.
 */
public enum Message {
    // Messages related to the plugin's lifecycle.
    PLUGIN_ENABLED("Plugin zostal wlaczony!"),
    PLUGIN_DISABLED("Plugin zostal wylaczony!"),

    // Messages related to the generator.
    GENERATOR_NAME("§6Generator Stone"),
    GENERATOR_PLACED("§aPomyślnie postawiono generator stone!"),
    GENERATOR_DESTROYED("§aPomyślnie zniszczono generator stone!"),
    GENERATOR_CANNOT_BUILD("§cNie możesz stawiać bloków w miejscu generatora!"),
    GENERATOR_PREVENT_PLACE("§cNie możesz stawiać generatora w tym miejscu!"),

    // Messages related to commands and permissions.
    COMMAND_ONLY_FOR_PLAYERS("Tylko gracze moga uzywac tej komendy!"),
    NO_PERMISSION_RELOAD("§cNie masz uprawnien do przeladowania pluginu!"),
    PLUGIN_RELOADED_PLAYER("§aPlugin zostal przeladowany!"),
    PLUGIN_RELOADED_CONSOLE("[xlucashDROP] Plugin zostal przeladowany!"),

    // Messages related to the GUI.
    DROP_ENABLED("§7Drop: §aWłączony"),
    DROP_DISABLED("§7Drop: §cWyłączony"),
    GUI_TITLE("Drop ze stone"),
    EMPTY_SLOT_NAME(" "),
    CHANCE_LORE("Szansa: %s%%"),
    FAST_CLICK("§cNie klikaj tak szybko!"),
    DROP_STATUS_CHANGED("§7Status dropu %s §7został zmieniony na: %s"),
    STATUS_ENABLED("§aWłączony"),
    STATUS_DISABLED("§cWyłączony"),

    // Messages related to hooks.
    SUPERIORSKYBLOCK_HOOKED("Hook z SuperiorSkyblock zostal zaladowany!"),
    SUPERIORSKYBLOCK_HOOK_FAILED("Hook z SuperiorSkyblock nie zostal zaladowany!"),

    // Messages related to database.
    DATABASE_CONNECTION_ERROR("Wystapil blad podczas laczenia z baza danych! Sprawdz konfiguracje pluginu!");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getText() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}