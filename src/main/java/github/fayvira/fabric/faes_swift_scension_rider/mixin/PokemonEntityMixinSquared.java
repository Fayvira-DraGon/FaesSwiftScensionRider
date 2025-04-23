package github.fayvira.fabric.faes_swift_scension_rider.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.cobblemon.mod.common.api.scheduling.Schedulable;
import com.cobblemon.mod.common.entity.PosableEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokedex.scanner.ScannableEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import dev.zanckor.cobblemonridingfabric.MCUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = PokemonEntity.class, priority = 1500)
public abstract class PokemonEntityMixinSquared extends TameableShoulderEntity implements PosableEntity, Shearable, Schedulable, ScannableEntity {
  @Shadow(remap = false)
  public abstract Pokemon getPokemon();

  @Shadow(remap = false)
  public float speedMultiplier;

  protected PokemonEntityMixinSquared(EntityType<? extends TameableShoulderEntity> entityType, World world) {
    super(entityType, world);
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "flyingHandler")
  @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(doubleValue = 0.3))
  private double onFlyingHandler$positive(double value) {
    return 0.5 * speedMultiplier;
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "flyingHandler")
  @ModifyConstant(method = "@MixinSquared:Handler", constant = @Constant(doubleValue = -0.3))
  private double onFlyingHandler$negative(double value) {
    return -0.7 * speedMultiplier;
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "travelHandler")
  @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 1), index = 1)
  private double onTravelHandler$move(double value) {
    return MCUtil.getPassengerObject(this.getPokemon().getSpecies().getName(), this.getPokemon().getForm().getName()).getSpeedModifier();
  }

  @TargetHandler(mixin = "dev.zanckor.cobblemonridingfabric.mixin.PokemonMixin", name = "travelHandler")
  @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;multiply(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 2), index = 1)
  private double onTravelHandler$setVelocity(double value) {
    return MCUtil.getPassengerObject(this.getPokemon().getSpecies().getName(), this.getPokemon().getForm().getName()).getSpeedModifier();
  }
}
