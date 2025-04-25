package github.fayvira.fabric.faes_swift_scension_rider;

import github.fayvira.fabric.faes_swift_scension_rider.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

import static github.fayvira.fabric.faes_swift_scension_rider.FaesSwiftScensionRider.*;

public class FaesSwiftScensionRiderClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ModConfig.clearServerConfig());

    ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
      @Override
      public Identifier getFabricId() {
        return Identifier.of(MOD_ID, "reload_listener");
      }

      @Override
      public void reload(ResourceManager manager) {
        ModConfig.loadFromFile();

        ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
        List<String> serverPackIds = resourcePackManager.getProfiles().stream().filter(profile -> profile.getSource() == ResourcePackSource.SERVER).map(ResourcePackProfile::getId).toList();
        for (Map.Entry<Identifier, List<Resource>> entry : manager.findAllResources("config", identifier -> identifier.getPath().equals("config/" + MOD_ID + ".json")).entrySet()) {
          for (Resource resource : entry.getValue()) {
            if (!serverPackIds.contains(resource.getPackId())) continue;

            try {
              ModConfig.loadServerConfig(resource.getReader());
            } catch (Exception e) {
              LOGGER.error("Failed to load server config file \"{}\" from pack \"{}\"",
                entry.getKey(), resource.getPackId());
              LOGGER.error("Error: ", e);
            }
          }
        }
      }
    });

    LOGGER.info("Initializing Client for Mod: {}.", MOD_NAME);
  }
}
