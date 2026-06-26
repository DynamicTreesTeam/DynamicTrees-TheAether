package com.dtteam.dttwilightforest.canceller;

import com.dtteam.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.dtteam.dynamictrees.worldgen.featurecancellation.TreeFeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SimpleFeatureCanceller<T extends FeatureConfiguration> extends TreeFeatureCanceller<T> {

    private final Class<T> treeFeatureConfigClass;

    public SimpleFeatureCanceller(final ResourceLocation registryName, Class<T> treeFeatureConfigClass) {
        super(registryName, treeFeatureConfigClass);
        this.treeFeatureConfigClass = treeFeatureConfigClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.NormalFeatureCancellation featureCancellations) {
        final FeatureConfiguration featureConfig = configuredFeature.config();

        return treeFeatureConfigClass.isInstance(featureConfig);
    }

}
