package github.fayvira.fabric.faes_swift_scension_rider;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("CommentedOutCode")
public class FaesSwiftScensionRider implements ModInitializer {
  public static final String MOD_ID = "faes_swift_scension_rider";
  public static final String MOD_NAME = "Fae's Swift 'Scension Rider";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  @Override
  public void onInitialize() {
    LOGGER.info("Initializing Mod: {}.", MOD_NAME);
  }

  // public static Identifier of(String path) {
  //   return Identifier.of(MOD_ID, path);
  // }
}
