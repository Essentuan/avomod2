package cf.avicia.avomod2.client.configs;

import cf.avicia.avomod2.core.CustomFile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;

public class ConfigsHandler {

    public static JsonObject configs = null;
    private static CustomFile configsFile = null;


    public static Config[] configsArray = new Config[]{
            new ConfigToggle("General", "Disable Everything", "Disabled", "disableAll"),
            new ConfigToggle("General", "(NOT IMPLEMENTED)Notify for AvoMod BETA Version (may have bugs)", "Disabled", "betaNotification"),
            new ConfigToggle("Guild", "Filter Out Bank Messages", "Disabled", "filterBankMessages"),
            new ConfigToggle("Guild", "Filter Out All Resource Messages", "Disabled", "filterResourceMessages"),
            new ConfigToggle("Chat", "Reveal Nicknames", "Enabled", "revealNicks"),
            new ConfigToggle("Chat", "Auto Skip Quest Dialogue", "Disabled", "skipDialogue"),
            new ConfigToggle("Chat", "Show Chat Timestamps", "Enabled", "chatTimestamps"),
            new ConfigToggle("Chat", "Filter Out Welcome Message", "Disabled", "filterWelcomeMessage"),
            new ConfigToggle("Chat", "(NOT IMPLEMENTED)Click to Say Congrats Message", "Enabled", "clickToSayCongrats"),
            new ConfigInput("Chat", "(NOT IMPLEMENTED)Click to say congrats message", "Congrats!", ".+", "^.+$", 30, "congratsMessage"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Custom Attack Timers Display", "Enabled", "attacksMenu"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Green Beacon at Soonest War", "Enabled", "greenBeacon"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Announce Territory Defense in Chat", "Enabled", "terrDefenseInChat"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Display War Info (dps, tower ehp, etc.)", "Enabled", "dpsInWars"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Hide Entities in Wars", "Disabled", "hideEntitiesInWar"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Aura Ping", "Enabled", "auraPing"),
            new ConfigInput("War", "(NOT IMPLEMENTED)Aura Ping Color", "FF6F00", "[\\da-fA-F]+", "^[\\da-fA-F]{6}$", 6, "auraPingColor"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Display Weekly Warcount on Screen", "Disabled", "displayWeeklyWarcount"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Prevent joining wars when afk", "Enabled", "afkWarProtection"),
            new ConfigInput("War", "(NOT IMPLEMENTED)Minutes until considered afk", "10", "[0-9]+", "^[0-9]+$", 3, "afkTime"),
            new ConfigInput("War", "(NOT IMPLEMENTED)Territory attack confirmation threshold", "15000", "[0-9]+", "^[0-9]+$", 6, "attackConfirmation"),
            new ConfigToggle("War", "(NOT IMPLEMENTED)Send defenses from attacked territories to server (improves accuracy of timer list for guild members)", "Enabled", "storeDefs"),
            new ConfigToggle("Misc", "Auto /stream on World Swap", "Disabled", "autoStream"),
            new ConfigToggle("Misc", "(NOT IMPLEMENTED)Prevent Moving Armor/Accessories", "Disabled", "disableMovingArmor"),
            new ConfigToggle("Misc", "Make Mob Health Bars More Readable", "Enabled", "readableHealth"),
            new ConfigToggle("Misc", "(NOT IMPLEMENTED)Display Some Tab Stats on Screen", "Disabled", "tabStatusDisplay"),
            new ConfigToggle("Misc", "(NOT IMPLEMENTED)Bomb Bell Tracker (REQUIRES CHAMPION)", "Enabled", "bombBellTracker"),
            new ConfigToggle("Misc", "(NOT IMPLEMENTED)Bomb Bell Tracker - Click to Switch World", "Enabled", "bombBellSwitchWorld")
    };
    public static void initializeConfigs() {
        configsFile = new CustomFile(getConfigPath("configs"));
        JsonObject configsJson = configsFile.readJson();
        boolean configsChanged = false;

        for (Config config : configsArray) {
            JsonElement configElement = configsJson.get(config.configsKey);

            if (configElement == null || configElement.isJsonNull()) {
                configsJson.addProperty(config.configsKey, config.defaultValue);
                configsChanged = true;
            }
        }

        if (configsChanged) {
            configsFile.writeJson(configsJson);
        }
        configs = configsJson;
    }

    public static String getConfigPath(String name) {
        return String.format("avomod/%s/%s.json", MinecraftClient.getInstance().getSession().getUuid().replaceAll("-", ""), name);
    }

    public static String getConfig(String configKey) {
        JsonElement configElement = configs.get(configKey);

        if (configElement == null || configElement.isJsonNull()) {
            return "";
        } else {
            return configElement.getAsString();
        }
    }

    public static void updateConfigs(String configsKey, String newValue) {
        JsonObject configsJson = configsFile.readJson();
        configsJson.addProperty(configsKey, newValue);

        if (configsKey.equals("autoStream") && newValue.equals("Disabled")) {
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.sendChatMessage("/stream");
            }
        }

        ConfigsHandler.configs = configsJson;
        configsFile.writeJson(configsJson);
    }

    public static boolean getConfigBoolean(String configKey) {
        String configValue = getConfig(configKey);

        return configValue.equals("Enabled");
    }
}
