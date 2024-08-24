package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.block.branch.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class JellyshroomBranchesGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");
    public static final ConfigurationProperty<Integer> BLOCKS_PER_BRANCH = ConfigurationProperty.integer("blocks_per_branch");

    public JellyshroomBranchesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }
    @Override
    protected void registerProperties() {
        this.register(BLOCK, PLACE_CHANCE, BLOCKS_PER_BRANCH);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, Blocks.AIR)
                .with(BLOCKS_PER_BRANCH, 10)
                .with(PLACE_CHANCE, 0.1f);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        BlockPos.MutableBlockPos testPos = context.pos().above().mutable();
        int h = 0;
        while (TreeHelper.isBranch(context.level().getBlockState(testPos))){
            h++;
            testPos.move(0,1,0);
        }
        if (h == 0) return false;
        return placeAllSideBranches(h, configuration.get(BLOCKS_PER_BRANCH), 1, context.level(), context.pos(), configuration.get(BLOCK), true);
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (!context.natural() || context.fertility() == 0) return false;
        final BlockPos rootPos = context.pos();
        final FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(context.level(), rootPos, new MapSignal(endFinder));
        final List<BlockPos> endPoints = endFinder.getEnds();
        if (endPoints.isEmpty()) return false;
        int height = endPoints.get(0).getY() - rootPos.getY();
        return placeAllSideBranches(height, configuration.get(BLOCKS_PER_BRANCH), configuration.get(PLACE_CHANCE), context.level(), context.pos(), configuration.get(BLOCK), false);
    }

    private boolean placeAllSideBranches(int height, int hash, float chance, LevelAccessor level, BlockPos rootPos, Block block, boolean worldgen){
        for (int y=2; y < height; y++){
            for (Direction dir : Direction.Plane.HORIZONTAL){
                BlockPos pos = rootPos.above(y).offset(dir.getNormal());
                BlockState existing = level.getBlockState(pos);
                if (CoordUtils.coordHashCode(pos, 2) % hash == 0
                        && (existing.canBeReplaced() || existing.getBlock() instanceof TrunkShellBlock)
                        && level.getRandom().nextFloat() < chance){
                    level.setBlock(pos, block.defaultBlockState(), 3);
                    if (!worldgen) return true;
                }
            }
        }
        return true;
    }

}
