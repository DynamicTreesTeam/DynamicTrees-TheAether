package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.api.treedata.TreePart;
import com.ferreusveritas.dynamictrees.systems.GrowSignal;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zepalesque.redux.block.ReduxBlocks;
import net.zepalesque.redux.client.audio.ReduxSoundEvents;

public class JellyshroomCapProperties extends DropBlocksCapProperties {

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(JellyshroomCapProperties::new);

    public JellyshroomCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public BlockBehaviour.Properties getDefaultBlockProperties(MapColor mapColor) {
        return super.getDefaultBlockProperties(mapColor).noOcclusion().isSuffocating(ReduxBlocks::never).isViewBlocking(ReduxBlocks::never);
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
            public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
                return Shapes.empty();
            }
            public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
                return true;
            }
            public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
                return properties.isPartOfCap(pAdjacentBlockState) || super.skipRendering(pState, pAdjacentBlockState, pSide);
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
            public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
                return Shapes.empty();
            }
            public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
                return true;
            }
            public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
                return properties.isPartOfCap(pAdjacentBlockState) || super.skipRendering(pState, pAdjacentBlockState, pSide);
            }

            @Override
            public boolean tryGrowCap(Level level, CapProperties capProp, int currentAge, GrowSignal signal, BlockPos pos, BlockPos previousPos, boolean forceAge) {
                Species var9 = signal.getSpecies();
                if (var9 instanceof HugeMushroomSpecies species) {
                    if (!level.isEmptyBlock(pos) && !(level.getBlockState(pos).getBlock() instanceof DynamicCapBlock)) {
                        TreePart treePart = TreeHelper.getTreePart(level.getBlockState(pos));
                        return treePart instanceof DynamicCapCenterBlock;
                    } else {
                        int age = currentAge;
                        if (currentAge == 0) {
                            age = 1;
                        } else if (forceAge || level.getRandom().nextFloat() < species.getChanceToAge()) {
                            age = Math.min(age + 1, this.properties.getMaxAge(species));
                        }

                        level.setBlock(pos, this.getCapBlockStateForPlacement(level, pos, age == 0 ? 1 : age, capProp.getDynamicCapState(true), false), 2);
                        if (age != currentAge) {
                            this.ageBranchUnderCap(level, pos, signal, currentAge);
                        }

                        if (!(signal.getSpecies() instanceof HugeMushroomSpecies)) {
                            return false;
                        } else {
                            this.generateCap(age, level, (HugeMushroomSpecies)signal.getSpecies(), pos, previousPos, currentAge, signal.rootPos);
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
        };
    }

}
