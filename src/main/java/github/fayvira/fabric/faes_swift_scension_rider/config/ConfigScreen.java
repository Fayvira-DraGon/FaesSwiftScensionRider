package github.fayvira.fabric.faes_swift_scension_rider.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.function.Supplier;

import static github.fayvira.fabric.faes_swift_scension_rider.FaesSwiftScensionRider.MOD_ID;
import static github.fayvira.fabric.faes_swift_scension_rider.FaesSwiftScensionRider.MOD_NAME;

public class ConfigScreen {
  public static Screen buildScreen(Screen parent) {
    final ConfigBuilder builder = ConfigBuilder.create()
      .setParentScreen(parent)
      .setTitle(Text.literal(MOD_NAME));

    final ConfigEntryBuilder entryBuilder = builder.entryBuilder();
    final ConfigCategory verticalSpeed = builder.getOrCreateCategory(Text.literal("Vertical Speed"));

    /* Vertical Speed */

    verticalSpeed.addEntry(basicFloat(entryBuilder, "up_speed"));
    verticalSpeed.addEntry(basicFloat(entryBuilder, "down_speed"));

    /* Other Tweaks */

    // TODO

    builder.setSavingRunnable(ModConfig::saveConfig);
    return builder.build();
  }

  private static AbstractConfigListEntry<?> basicFloat(ConfigEntryBuilder builder, String key) {
    //noinspection UnstableApiUsage
    return builder.startFloatField(Text.translatable("%s.config.option.%s".formatted(MOD_ID, key)), ModConfig.getFloat(key))
      .setRequirement(() -> !ModConfig.serverOverride(key))
      .setDefaultValue(0.3F)
      .setTooltipSupplier(tooltip(key))
      .setSaveConsumer(value -> ModConfig.setFloat(key, value))
      .build();
  }

  private static Supplier<Optional<Text[]>> tooltip(String key) {
    return () -> {
      if (ModConfig.serverOverride(key)) {
        return Optional.of(new Text[] { Text.translatable(MOD_ID + ".config.option.overridden_tooltip") });
      }
      return Optional.of(new Text[] { Text.translatable("%s.config.option.%s.tooltip".formatted(MOD_ID, key)) });
    };
  }
}
