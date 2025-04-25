package github.fayvira.fabric.faes_swift_scension_rider.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static github.fayvira.fabric.faes_swift_scension_rider.FaesSwiftScensionRider.LOGGER;
import static github.fayvira.fabric.faes_swift_scension_rider.FaesSwiftScensionRider.MOD_ID;

public class ModConfig {
  private static final Gson GSON = new Gson().newBuilder().setPrettyPrinting().create();
  public static final JsonObject INTERNAL_CONFIG = new JsonObject();
  public static final JsonObject SERVER_CONFIG = new JsonObject();
  public static final JsonObject DEFAULT_CONFIG = new JsonObject();

  static {
    DEFAULT_CONFIG.addProperty("up_speed", 0.3);
    DEFAULT_CONFIG.addProperty("down_speed", 0.3);
  }

  public static void loadFromFile() {
    INTERNAL_CONFIG.asMap().clear();

    try (FileReader reader = new FileReader(getConfigFile())) {
      if (JsonParser.parseReader(reader) instanceof JsonObject jsonObject) {
        jsonObject.entrySet().forEach(entry -> INTERNAL_CONFIG.add(entry.getKey(), entry.getValue()));
      }
    } catch (Exception e) {
      LOGGER.warn("Error occurred while loading Config!");
      LOGGER.warn(e.getMessage());
    }
  }

  public static void clearServerConfig() {
    SERVER_CONFIG.asMap().clear();
  }

  public static void loadServerConfig(BufferedReader reader) {
    if (JsonParser.parseReader(reader) instanceof JsonObject jsonObject) {
      jsonObject.entrySet().forEach(entry -> SERVER_CONFIG.add(entry.getKey(), entry.getValue()));
    }
  }

  public static boolean serverOverride(String option) {
    return SERVER_CONFIG.get(option) instanceof JsonPrimitive primitive && primitive.isBoolean();
  }

  public static float getFloat(String option) {
    if (SERVER_CONFIG.get(option) instanceof JsonPrimitive primitive && primitive.isNumber()) {
      return Math.abs(primitive.getAsFloat());
    }

    return INTERNAL_CONFIG.get(option) instanceof JsonPrimitive primitive && primitive.isNumber()
      ? Math.abs(primitive.getAsFloat())
      : Math.abs(DEFAULT_CONFIG.get(option).getAsFloat());
  }

  public static void setFloat(String key, float value) {
    INTERNAL_CONFIG.addProperty(key, Math.abs(value));
  }

  public static void saveConfig() {
   try (FileWriter fileWriter = new FileWriter(getConfigFile())) {
      GSON.toJson(INTERNAL_CONFIG, fileWriter);
      fileWriter.flush();
    } catch (IOException e) {
      LOGGER.warn("Error occurred while saving config!");
      LOGGER.warn(e.getMessage());
    }
  }

  public static File getConfigFile() {
    File configFile = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json").toFile();
    if (!configFile.exists()) {
      try {
        //noinspection ResultOfMethodCallIgnored
        configFile.getParentFile().mkdirs();
        if (!configFile.createNewFile()) {
          throw new IOException();
        }

        // final File oldFile = FabricLoader.getInstance().getConfigDir().resolve("dystorian_tweaks.json").toFile();
        // if (oldFile.isFile()) {
        //   try (FileReader reader = new FileReader(oldFile)) {
        //     if (JsonParser.parseReader(reader) instanceof JsonObject jsonObject) {
        //       jsonObject.entrySet().forEach(entry -> INTERNAL_CONFIG.add(entry.getKey(), entry.getValue()));
        //     }
        //     saveConfig();
        //   } catch (Exception e) {
        //     LOGGER.warn("Error occurred while loading old Config!");
        //     LOGGER.warn(e.getMessage());
        //   }
        // } else {
          DEFAULT_CONFIG.entrySet().forEach(entry -> INTERNAL_CONFIG.add(entry.getKey(), entry.getValue()));
          saveConfig();
        // }
      } catch(IOException | SecurityException e) {
        LOGGER.warn("Failed to create config file!");
        LOGGER.warn(e.getMessage());
      }
    }
    return configFile;
  }
}
