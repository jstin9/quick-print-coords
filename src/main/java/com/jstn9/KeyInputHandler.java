package com.jstn9;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.sound.SoundEvents;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class KeyInputHandler {
    private static boolean showCoordinates = true;
    private static boolean showDeathMessage = true;
    private static String coordinatesTemplate = "My cords: {X}, {Y}, {Z}, {DIMENSION}";
    private static String deathMessageTemplate = "You died at: {X}, {Y}, {Z}, {DIMENSION}";
    private static String overworldText = "Overworld";
    private static String netherText = "Nether";
    private static String endText = "End";
    private static boolean deathCoordinatesShow = false;
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "quick-print-coords.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void handleKeyInput(MinecraftClient client, boolean toSelf) {
        if (client.player != null && showCoordinates) {
            String coordinatesMessage = formatCoordinatesMessage(client, coordinatesTemplate);
            if (toSelf) {
                client.player.sendMessage(Text.of(coordinatesMessage), false);
            } else {
                client.player.networkHandler.sendChatMessage(coordinatesMessage);
            }
            playSound(client);
        }
    }

    private static String formatCoordinatesMessage(MinecraftClient client, String template) {
        double x = client.player.getX();
        double y = client.player.getY();
        double z = client.player.getZ();
        String currentDimension = getDimensionName(client);

        return template.replace("{X}", String.format("%.0f", x))
                .replace("{Y}", String.format("%.0f", y))
                .replace("{Z}", String.format("%.0f", z))
                .replace("{DIMENSION}", currentDimension);
    }

    private static void playSound(MinecraftClient client) {
        assert client.player != null;
        client.player.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }

    private static String getDimensionName(MinecraftClient client) {
        if (client.world == null) return "";
        return switch (client.world.getRegistryKey().getValue().toString()) {
            case "minecraft:overworld" -> overworldText;
            case "minecraft:the_nether" -> netherText;
            case "minecraft:the_end" -> endText;
            default -> "Unknown";
        };
    }

    public static void showDeathCoordinates(MinecraftClient client) {
        if (client.player != null && showDeathMessage && !deathCoordinatesShow) {
            String deathMessage = formatCoordinatesMessage(client, deathMessageTemplate);
            client.player.sendMessage(Text.of(deathMessage));
            deathCoordinatesShow = true;
        }
    }

    public static void resetDeathCoordinatesFlag() {
        deathCoordinatesShow = false;
    }

    public static void loadConfig() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ConfigData config = GSON.fromJson(reader, ConfigData.class);
                showCoordinates = config.showCoordinates;
                showDeathMessage = config.showDeathMessage;
                coordinatesTemplate = config.coordinatesTemplate;
                deathMessageTemplate = config.deathMessageTemplate;
                overworldText = config.overworldText;
                netherText = config.netherText;
                endText = config.endText;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveConfig();
        }
    }

    public static void saveConfig() {
        ConfigData config = new ConfigData(showCoordinates, showDeathMessage, coordinatesTemplate, deathMessageTemplate, overworldText, netherText, endText);
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ConfigData {
        boolean showCoordinates;
        boolean showDeathMessage;
        String coordinatesTemplate;
        String deathMessageTemplate;
        String overworldText;
        String netherText;
        String endText;

        ConfigData(boolean showCoordinates, boolean showDeathMessage, String coordinatesTemplate, String deathMessageTemplate,
                   String overworldText, String netherText, String endText) {
            this.showCoordinates = showCoordinates;
            this.showDeathMessage = showDeathMessage;
            this.coordinatesTemplate = coordinatesTemplate;
            this.deathMessageTemplate = deathMessageTemplate;
            this.overworldText = overworldText;
            this.netherText = netherText;
            this.endText = endText;
        }
    }

    // Геттеры и сеттеры для конфигурации
    public static boolean isShowDeathMessage() {
        return showDeathMessage;
    }

    public static void setShowDeathMessage(boolean showDeathMessage) {
        KeyInputHandler.showDeathMessage = showDeathMessage;
        saveConfig();
    }

    public static String getDeathMessageTemplate() {
        return deathMessageTemplate;
    }

    public static void setDeathMessageTemplate(String newTemplate) {
        deathMessageTemplate = newTemplate;
        saveConfig();
    }

    public static boolean isShowCoordinates() {
        return showCoordinates;
    }

    public static void setShowCoordinates(boolean showCoordinates) {
        KeyInputHandler.showCoordinates = showCoordinates;
        saveConfig();
    }

    public static String getOverworldText() {
        return overworldText;
    }

    public static void setOverworldText(String text) {
        overworldText = text;
        saveConfig();
    }

    public static String getNetherText() {
        return netherText;
    }

    public static void setNetherText(String text) {
        netherText = text;
        saveConfig();
    }

    public static String getEndText() {
        return endText;
    }

    public static void setEndText(String text) {
        endText = text;
        saveConfig();
    }

    public static String getCoordinatesTemplate() {
        return coordinatesTemplate;
    }

    public static void setCoordinatesTemplate(String newTemplate) {
        coordinatesTemplate = newTemplate;
        saveConfig();
    }
}
