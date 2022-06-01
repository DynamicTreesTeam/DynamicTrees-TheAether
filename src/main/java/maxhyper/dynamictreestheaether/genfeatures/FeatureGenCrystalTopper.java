package maxhyper.dynamictreestheaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.treedata.ILeavesProperties;
import com.ferreusveritas.dynamictrees.systems.featuregen.FeatureGenConiferTopper;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FeatureGenCrystalTopper extends FeatureGenConiferTopper {

    public FeatureGenCrystalTopper(ILeavesProperties leavesProperties) {
        super(leavesProperties);
    }

    public boolean postGeneration(World world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, IBlockState initialDirtState) {
        if (endPoints == null || endPoints.isEmpty()) return false;
        BlockPos highest = Collections.max(endPoints, Comparator.comparingInt(Vec3i::getY));
        world.setBlockState(highest.up(1), this.leavesProperties.getDynamicLeavesState(4));
        world.setBlockState(highest.up(2), this.leavesProperties.getDynamicLeavesState(3));
        world.setBlockState(highest.up(3), this.leavesProperties.getDynamicLeavesState(1));
        return true;
    }

}
