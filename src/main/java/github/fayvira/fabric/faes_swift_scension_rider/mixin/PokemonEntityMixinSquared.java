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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = PokemonEntity.class, priority = 1500)
public abstract class PokemonEntityMixinSquared extends TameableShoulderEntity implements PosableEntity, Shearable, Schedulable, ScannableEntity {
  @SuppressWarnings("all")
  @Shadow(remap = false)
  private float speedMultiplier;

  protected PokemonEntityMixinSquared(EntityType<? extends TameableShoulderEntity> entityType, World world) {
    super(entityType, world);
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "flyingHandler")
  @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(doubleValue = 0.3))
  private double onFlyingHandler$positive(double value) {
    return this.speedMultiplier;
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "flyingHandler")
  @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(doubleValue = -0.3))
  private double onFlyingHandler$negative(double value) {
    return -1.0 * this.speedMultiplier;
  }
}
