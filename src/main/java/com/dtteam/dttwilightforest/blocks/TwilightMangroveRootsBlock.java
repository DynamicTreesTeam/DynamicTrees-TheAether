package com.dtteam.dttwilightforest.blocks;

import com.dtteam.dynamictrees.block.branch.BasicRootsBlock;
import com.dtteam.dttwilightforest.trees.TwilightMangroveFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class TwilightMangroveRootsBlock extends BasicRootsBlock {

    public static final BooleanProperty GRASSY = BooleanProperty.create("grassy");

    public TwilightMangroveRootsBlock(ResourceLocation name, Properties properties) {
        super(name, properties.randomTicks());
        registerDefaultState(defaultBlockState().setValue(GRASSY, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
       super.createBlockStateDefinition(builder);
       builder.add(GRASSY);
    }

    @Override
    public TwilightMangroveFamily getFamily() {
        return (TwilightMangroveFamily)super.getFamily();
    }

    private Optional<Block> getPrimitiveGrassIfGrassy (BlockState state){
        if (isFullBlock(state) && state.hasProperty(GRASSY) && state.getValue(GRASSY)){
            return getFamily().getPrimitiveGrassyRoots();
        }
        return Optional.empty();
    }

    private Block getPrimitiveAny (BlockState state){
        return getPrimitiveGrassIfGrassy(state).orElseGet(
                () -> state.getValue(LAYER).getPrimitive(getFamily()).orElse(null));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {

        if (isFullBlock(state)){
            //We override this function to change setValue to EXPOSED instead of FILLED (twilight mangrove roots cannot be filled)
            level.setBlock(pos, state.setValue(LAYER, Layer.EXPOSED).setValue(GRASSY, false), level.isClientSide ? 11 : 3);
            this.spawnDestroyParticles(level, player, pos, state);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            Block primitive = getPrimitiveAny(state);
            if (!player.isCreative() && primitive != null) dropResources(primitive.defaultBlockState(), level, pos, null, player, player.getMainHandItem());
            return false;
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public int setRadius(LevelAccessor level, BlockPos pos, int radius, @javax.annotation.Nullable Direction originDir, int flags) {
        int rad = super.setRadius(level, pos, radius, originDir, flags);
        BlockState newBranchState = level.getBlockState(pos);
        if (newBranchState.is(this) && newBranchState.getValue(LAYER) == Layer.COVERED){
            level.setBlock(pos, newBranchState.setValue(GRASSY, canBeGrassy(level, pos)), flags);
        }
        return rad;
    }

    protected boolean canBeGrassy(LevelAccessor level, BlockPos pos) {
        BlockPos upPos = pos.above();
        BlockState upState = level.getBlockState(upPos);
        return !upState.isCollisionShapeFullBlock(level, upPos) && upState.getFluidState().isEmpty();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        //if it's not covered don't tick.
        if (!state.is(this) || state.getValue(LAYER) != Layer.COVERED) return;
        int requiredLight = getFamily().getGrassSpreadRequiredLight();
        //this is a similar behaviour to vanilla grass spreading but inverted to be handled by the dirt block
        if (!level.isClientSide) {
            if (!level.isAreaLoaded(pos, 3)) {
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            }
            if (!canBeGrassy(level, pos)){
                level.setBlock(pos, state.setValue(GRASSY, false), 3);
            } else if (level.getMaxLocalRawBrightness(pos.above()) >= requiredLight) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos thatPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                    if (!level.hasChunkAt(thatPos)) { return; }
                    BlockState thatState = level.getBlockState(thatPos);

                    Block block = getFamily().getPrimitiveGrassyRoots().orElse(null);
                    if (block != null && thatState.getBlock() == block) {
                        level.setBlock(pos, state.setValue(GRASSY, true), 3);
                        return;
                    }
                }
            }
        }

    }

    //to-do: port to base DT
    @Override
    public TriState canSustainPlant(BlockState state, net.minecraft.world.level.BlockGetter world, BlockPos pos,
                                    Direction facing, BlockState plant) {
        return state.getValue(LAYER) == Layer.COVERED
                ? TriState.TRUE
                : super.canSustainPlant(state, world, pos, facing, plant);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        Optional<Block> primitiveGrass = getPrimitiveGrassIfGrassy(state);
        if (primitiveGrass.isPresent())
            return primitiveGrass.get().getSoundType(state, level, pos, entity);
        return super.getSoundType(state, level, pos, entity);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        Optional<Block> primitiveGrass = getPrimitiveGrassIfGrassy(state);
        if (primitiveGrass.isPresent())
            return primitiveGrass.get().getCloneItemStack(level, pos, state);
        return super.getCloneItemStack(level, pos, state);
    }

}
