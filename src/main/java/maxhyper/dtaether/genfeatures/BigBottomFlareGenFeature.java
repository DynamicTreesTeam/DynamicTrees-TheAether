package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.systems.genfeature.BottomFlareGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
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

        int i;
        int lastRad = 0;
        for (i=0; i<curve.length; i++){
            lastRad = TreeHelper.getRadius(world, rootPos.above(1+i));
            if (lastRad < i + configuration.get(MIN_RADIUS)) break;
        }
        if (i == 0) return;
        for (int j=0; j<i; j++){
            int radFlare = curve[j+(curve.length-i)];
            int finalJ = j;
            int finalLastRad = lastRad;
            family.getBranch().ifPresent(branch-> branch.setRadius(
                            world, rootPos.above(1+ finalJ), finalLastRad + radFlare, Direction.UP));
        }

    }

}
