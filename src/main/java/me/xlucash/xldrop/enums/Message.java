package me.xlucash.xldrop.enums;

public enum Message {
    PLUGIN_ENABLED("[xlucashDrop] Plugin zostal wlaczony!"),
    PLUGIN_DISABLED("[xlucashDrop] Plugin zostal wylaczony!"),
    GENERATOR_NAME("§6Generator Stone"),
    COMMAND_ONLY_FOR_PLAYERS("Tylko gracze moga uzywac tej komendy!"),
    NO_PERMISSION_RELOAD("§cNie masz uprawnien do przeladowania pluginu!"),
    PLUGIN_RELOADED_PLAYER("§aPlugin zostal przeladowany!"),
    PLUGIN_RELOADED_CONSOLE("[xlucashDROP] Plugin zostal przeladowany!"),
    DROP_ENABLED("§7Drop: §aWłączony"),
    DROP_DISABLED("§7Drop: §cWyłączony"),
    GUI_TITLE("Drop ze stone"),
    EMPTY_SLOT_NAME(" "),
    CHANCE_LORE("Szansa: %s%%"),
    GENERATOR_PLACED("§aPomyślnie postawiono generator stone!"),
    GENERATOR_DESTROYED("§aPomyślnie zniszczono generator stone!"),
    GENERATOR_CANNOT_BUILD("§cNie możesz stawiać bloków w miejscu generatora!"),
    FAST_CLICK("§cNie klikaj tak szybko!"),
    DROP_STATUS_CHANGED("§7Status dropu %s §7został zmieniony na: %s"),
    STATUS_ENABLED("§aWłączony"),
    STATUS_DISABLED("§cWyłączony"),
    SUPERIORSKYBLOCK_HOOKED("Hook z SuperiorSkyblock zostal zaladowany!"),
    SUPERIORSKYBLOCK_HOOK_FAILED("Hook z SuperiorSkyblock nie zostal zaladowany!");

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