package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.VinesGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.ferreusveritas.dynamictrees.systems.nodemapper.FindEndsNode;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class HangerVinesGenFeature extends VinesGenFeature {

    public HangerVinesGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(VINE_TYPE, VineType.CEILING);
    }

    @Override
    public boolean shouldApply(Species species, GenFeatureConfiguration configuration) {
        return configuration.get(VINE_TYPE) == VineType.CEILING;
    }

    protected void addVerticalVines(GenFeatureConfiguration configuration, LevelAccessor level, Species species, BlockPos rootPos, BlockPos branchPos, SafeChunkBounds safeBounds, boolean worldgen) {
        // Uses fruit ray trace method to grab a position under the tree's leaves.
        BlockPos vinePos = CoordUtils.getRayTraceFruitPos(level, species, rootPos, branchPos, safeBounds);

        if (!safeBounds.inBounds(vinePos, true)) {
            return;
        }

        if (vinePos == BlockPos.ZERO) {
            return;
        }

        this.placeVines(level, vinePos, configuration.get(BLOCK).defaultBlockState(),
                configuration.get(MAX_LENGTH),
                configuration.get(BLOCK).defaultBlockState(),
                VineType.CEILING, worldgen);
    }

}
