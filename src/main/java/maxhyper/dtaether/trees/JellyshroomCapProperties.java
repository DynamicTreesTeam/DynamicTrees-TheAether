package maxhyper.dtaether.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.zepalesque.redux.client.audio.ReduxSoundEvents;

public class JellyshroomCapProperties extends DropBlocksCapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(JellyshroomCapProperties::new);

    public JellyshroomCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicCapBlock createDynamicCap(BlockBehaviour.Properties properties) {
        return new DynamicCapBlock(this, properties){
            @Override
            public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
                return 0.5F;
            }
            @Override
            public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
                if (pEntity.isSuppressingBounce()) {
                    super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
                } else {
                    pEntity.causeFallDamage(pFallDistance, 0.0F, pLevel.damageSources().fall());
                    Player var10001;
                    if (pEntity instanceof Player) {
                        Player player = (Player)pEntity;
                        var10001 = player;
                    } else {
                        var10001 = null;
                    }
                    pLevel.playSound(var10001, pPos, ReduxSoundEvents.FUNGUS_BOUNCE.get(), SoundSource.BLOCKS, Math.min(pFallDistance / 10.0F, 0.8F), 0.9F + pLevel.random.nextFloat() * 0.2F);
                }
            }
            @Override
            public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
                if (pEntity.isSuppressingBounce()) {
                    super.updateEntityAfterFallOn(pLevel, pEntity);
                } else {
                    this.bounceUp(pEntity);
                }
            }
            private void bounceUp(Entity pEntity) {
                Vec3 vec3 = pEntity.getDeltaMovement();
                if (vec3.y < 0.0) {
                    double d0 = pEntity instanceof LivingEntity ? 0.95 : 0.85;
                    pEntity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
                }
            }
            @Override
            public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
                double d0 = Math.abs(pEntity.getDeltaMovement().y);
                if (d0 < 0.1 && !pEntity.isSteppingCarefully()) {
                    double d1 = 0.4 + d0 * 0.2;
                    pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(d1, 1.0, d1));
                }

                super.stepOn(pLevel, pPos, pState, pEntity);
            }
        };
    }

    @Override
    protected DynamicCapCenterBlock createDynamicCapCenter(BlockBehaviour.Properties properties) {
        return new DynamicCapCenterBlock(this, properties){
            @Override
            public float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
                return 0.5F;
            }
            @Override
            public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
                if (pEntity.isSuppressingBounce()) {
                    super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
                } else {
                    pEntity.causeFallDamage(pFallDistance, 0.0F, pLevel.damageSources().fall());
                    Player var10001;
                    if (pEntity instanceof Player) {
                        Player player = (Player)pEntity;
                        var10001 = player;
                    } else {
                        var10001 = null;
                    }
                    pLevel.playSound(var10001, pPos, ReduxSoundEvents.FUNGUS_BOUNCE.get(), SoundSource.BLOCKS, Math.min(pFallDistance / 10.0F, 0.8F), 0.9F + pLevel.random.nextFloat() * 0.2F);
                }
            }
            @Override
            public void updateEntityAfterFallOn(BlockGetter pLevel, Entity pEntity) {
                if (pEntity.isSuppressingBounce()) {
                    super.updateEntityAfterFallOn(pLevel, pEntity);
                } else {
                    this.bounceUp(pEntity);
                }
            }
            private void bounceUp(Entity pEntity) {
                Vec3 vec3 = pEntity.getDeltaMovement();
                if (vec3.y < 0.0) {
                    double d0 = pEntity instanceof LivingEntity ? 0.95 : 0.85;
                    pEntity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
                }
            }
            @Override
            public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
                double d0 = Math.abs(pEntity.getDeltaMovement().y);
                if (d0 < 0.1 && !pEntity.isSteppingCarefully()) {
                    double d1 = 0.4 + d0 * 0.2;
                    pEntity.setDeltaMovement(pEntity.getDeltaMovement().multiply(d1, 1.0, d1));
                }
                super.stepOn(pLevel, pPos, pState, pEntity);
            }
        };
    }

}
