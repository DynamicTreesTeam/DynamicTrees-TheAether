package com.dtteam.dttwilightforest.genfeatures;

import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.api.configuration.ConfigurationProperty;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGenerationContext;
import com.dtteam.dynamictrees.systems.genfeature.context.PostGrowContext;
import com.dtteam.dttwilightforest.trees.MagicFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

public class MagicCoreGenFeature extends GenFeature {

    // Min radius for the flare.
    public static final ConfigurationProperty<Integer> MIN_RADIUS = ConfigurationProperty.integer("min_radius");
    public static final ConfigurationProperty<Integer> HEIGHT = ConfigurationProperty.integer("height");

    public MagicCoreGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(MIN_RADIUS, HEIGHT);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(MIN_RADIUS, 6)
                .with(HEIGHT, 2);
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if (context.fertility() > 0 && context.species().getFamily() instanceof MagicFamily magicFamily) {
            this.flareBottom(configuration, context.level(), context.pos(), magicFamily);
            return true;
        }
        return false;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        if (context.species().getFamily() instanceof MagicFamily magicFamily){
            this.flareBottom(configuration, context.level(), context.pos(), magicFamily);
            return true;
        }
        return false;
    }

    /**
     * Put a cute little flare on the bottom of the dark oaks
     *
     * @param level   The level
     * @param rootPos The position of the rooty dirt block of the tree
     */
    public void flareBottom(GenFeatureConfiguration configuration, LevelAccessor level, BlockPos rootPos, MagicFamily family) {
        int height = configuration.get(HEIGHT);
        //Place the core on the 2nd block
        int radius2 = TreeHelper.getRadius(level, rootPos.above(height));

        if (radius2 > configuration.get(MIN_RADIUS)) {
            family.getCoreBranch().ifPresent(branch -> {
                BlockPos placePos = rootPos.above(height);
                if (!level.getBlockState(placePos).is(branch))
                    branch.setRadius(level, placePos, radius2, Direction.UP);
            });
        }
    }
}
