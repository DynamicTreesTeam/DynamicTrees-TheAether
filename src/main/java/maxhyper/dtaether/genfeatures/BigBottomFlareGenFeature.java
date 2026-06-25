package maxhyper.dtaether.genfeatures;

import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.systems.genfeature.BottomFlareGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nonnull;

public class BigBottomFlareGenFeature extends BottomFlareGenFeature {

    public BigBottomFlareGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    private static final int[] curve = {9,8,6,4,2,1};

    @Override
    public void flareBottom(@Nonnull GenFeatureConfiguration configuration, LevelAccessor world, BlockPos rootPos, Species species) {
        Family family = species.getFamily();

        int lastRad = TreeHelper.getRadius(world, rootPos.above(curve.length+1));
        int effectiveRad = Math.min(Math.max(0, lastRad - configuration.get(MIN_RADIUS)), curve.length);

        for (int j=0; j<effectiveRad; j++){
            int radFlare = curve[j + (curve.length-effectiveRad)];
            int finalJ = j;
            family.getBranch().ifPresent(branch-> branch.setRadius(world,
                    rootPos.above(1+ finalJ), lastRad + radFlare,
                    Direction.UP));
        }

    }

}
