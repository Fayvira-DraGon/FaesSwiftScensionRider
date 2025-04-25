package github.fayvira.fabric.faes_swift_scension_rider.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.cobblemon.mod.common.api.scheduling.Schedulable;
import com.cobblemon.mod.common.entity.PosableEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokedex.scanner.ScannableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static github.fayvira.fabric.faes_swift_scension_rider.config.ModConfig.getFloat;

@Mixin(value = PokemonEntity.class, priority = 3000)
public abstract class PokemonEntityMixinSquared extends TameableShoulderEntity implements PosableEntity, Shearable, Schedulable, ScannableEntity {
  protected PokemonEntityMixinSquared(EntityType<? extends TameableShoulderEntity> entityType, World world) {
    super(entityType, world);
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "flyingHandler")
  @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(doubleValue = 0.3), remap = false)
  private double onFlyingHandler$positive(double value) {
    return getFloat("up_speed");
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "flyingHandler")
  @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(doubleValue = -0.3), remap = false)
  private double onFlyingHandler$negative(double value) {
    return -1.0F * getFloat("down_speed");
  }
}
