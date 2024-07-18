package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.init.DTTrees;
import com.ferreusveritas.dynamictrees.systems.genfeature.BiomePredicateGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import net.minecraft.resources.ResourceLocation;

public class NegativeBiomePredicateGenFeature extends BiomePredicateGenFeature {
    public static final ConfigurationProperty<GenFeatureConfiguration> GEN_FEATURE2 = ConfigurationProperty.property("gen_feature_2", GenFeatureConfiguration.class);

    public NegativeBiomePredicateGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    protected void registerProperties() {
        this.register(BIOME_PREDICATE, GEN_FEATURE, GEN_FEATURE2, ONLY_WORLD_GEN);
    }

    @Override
    protected GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(GEN_FEATURE2, GenFeatureConfiguration.getNull());
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final boolean worldGen = context.isWorldGen();
        final GenFeatureConfiguration configurationToPlace1 = configuration.get(GEN_FEATURE);
        final GenFeatureConfiguration configurationToPlace2 = configuration.get(GEN_FEATURE2);
        if (configuration.getGenFeature().getRegistryName().equals(DTTrees.NULL)) return false;

        if (!(configuration.get(ONLY_WORLD_GEN) && !worldGen) &&
                //Negate the predicate test
                !configuration.get(BIOME_PREDICATE).test(context.biome())) {
            boolean placed1 = configurationToPlace1.generate(Type.POST_GENERATION, context);
            boolean placed2 = configurationToPlace2.generate(Type.POST_GENERATION, context);
            return placed1 && placed2;
        }

        return false;
    }
}
