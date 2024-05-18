package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class SnowyLeavesBlock extends DynamicLeavesBlock {
    public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

    private float leafChance = 0.66f;
    private int maxHydro = 1;

    public SnowyLeavesBlock(LeavesProperties leavesProperties, Properties properties, float leafChance, int maxHydro) {
        super(leavesProperties, properties);
        this.registerDefaultState(this.defaultBlockState().setValue(SNOWY, false));
        this.leafChance = leafChance;
        this.maxHydro = maxHydro;
    }

    @Override @NotNull
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        return facing == Direction.UP ? state.setValue(SNOWY, isSnowySetting(facingState)) : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().above());
        return super.getStateForPlacement(context).setValue(SNOWY, isSnowySetting(blockstate));
    }

    private static boolean isSnowySetting(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SNOWY);
    }

    public int getHydrationLevelFromNeighbors(LevelAccessor level, BlockPos pos, LeavesProperties leavesProperties) {
        int hydro = super.getHydrationLevelFromNeighbors(level, pos, leavesProperties);
        if (hydro <= maxHydro){
            int hash = CoordUtils.coordHashCode(pos, 2) % 1000;
            float rand = hash / 1000f;
            if (rand >= leafChance) return 0;
        }
        return hydro;
    }

    public BlockState getLeavesBlockStateForPlacement(LevelAccessor level, BlockPos pos, BlockState leavesStateWithHydro, int oldHydro, boolean worldGen) {
        return leavesStateWithHydro.setValue(SNOWY, false);
    }
}
