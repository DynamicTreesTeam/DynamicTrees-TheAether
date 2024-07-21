package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.init.DTTrees;
import com.ferreusveritas.dynamictrees.systems.genfeature.BiomePredicateGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.FullGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import net.minecraft.resources.ResourceLocation;

public class NegativeBiomePredicateGenFeature extends BiomePredicateGenFeature {
    public static final ConfigurationProperty<GenFeatureConfiguration> POST_GEN_FEATURE = ConfigurationProperty.property("post_gen_feature", GenFeatureConfiguration.class);

    public NegativeBiomePredicateGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    protected void registerProperties() {
        this.register(BIOME_PREDICATE, GEN_FEATURE, POST_GEN_FEATURE, ONLY_WORLD_GEN);
    }

    @Override
    protected GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration().with(POST_GEN_FEATURE, GenFeatureConfiguration.getNull());
    }

    @Override
    protected boolean generate(GenFeatureConfiguration configuration, FullGenerationContext context) {
        final GenFeatureConfiguration configurationToPlace = configuration.get(GEN_FEATURE);
        if (configuration.getGenFeature().getRegistryName().equals(DTTrees.NULL)) return false;

        if (!configuration.get(BIOME_PREDICATE).test(context.biome())) {
            return configurationToPlace.generate(Type.FULL, context);
        }

        return false;
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final boolean worldGen = context.isWorldGen();
        final GenFeatureConfiguration configurationToPlace = configuration.get(POST_GEN_FEATURE);
        if (configuration.getGenFeature().getRegistryName().equals(DTTrees.NULL)) return false;

        if (!(configuration.get(ONLY_WORLD_GEN) && !worldGen) &&
                //Negate the predicate test
                !configuration.get(BIOME_PREDICATE).test(context.biome())) {
            return configurationToPlace.generate(Type.POST_GENERATION, context);
        }

        return false;
    }
}
