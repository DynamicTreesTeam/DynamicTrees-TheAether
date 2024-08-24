package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.block.branch.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.BottomFlareGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;

public class SporesGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> BLOCK = ConfigurationProperty.block("block");

    public SporesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }
    @Override
    protected void registerProperties() {
        this.register(BLOCK, PLACE_CHANCE);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.01f);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        BlockPos.MutableBlockPos testPos = context.pos().above().mutable();
        while (TreeHelper.isBranch(context.level().getBlockState(testPos))){
            testPos.move(0,1,0);
        }
        BlockPos end = testPos.immutable().below();
        for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {
            BlockPos pos = end.offset(surr.getOffset());
            context.level().setBlock(pos, configuration.get(BLOCK).defaultBlockState(), 3);
        }
        return true;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (!context.natural() || context.fertility() == 0) return false;
        final BlockPos rootPos = context.pos();
        final FindEndsNode endFinder = new FindEndsNode();
        TreeHelper.startAnalysisFromRoot(context.level(), rootPos, new MapSignal(endFinder));
        final List<BlockPos> endPoints = endFinder.getEnds();
        if (endPoints.isEmpty()) return false;
        for (BlockPos end : endPoints){
            for (CoordUtils.Surround surr : CoordUtils.Surround.values()) {
                BlockPos pos = end.offset(surr.getOffset());
                BlockState existing = context.level().getBlockState(pos);
                if ((existing.canBeReplaced() || existing.getBlock() instanceof TrunkShellBlock)
                        && context.random().nextFloat() < configuration.get(PLACE_CHANCE)){
                    context.level().setBlock(pos, configuration.get(BLOCK).defaultBlockState(), 3);
                }

            }
        }
        return true;
    }
}
