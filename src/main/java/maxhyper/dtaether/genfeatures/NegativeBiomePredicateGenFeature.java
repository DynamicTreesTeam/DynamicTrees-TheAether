package maxhyper.dtaether.genfeatures;

import com.ferreusveritas.dynamictrees.init.DTTrees;
import com.ferreusveritas.dynamictrees.systems.genfeature.BiomePredicateGenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeature.context.PostGenerationContext;
import net.minecraft.resources.ResourceLocation;

public class NegativeBiomePredicateGenFeature extends BiomePredicateGenFeature {
    public NegativeBiomePredicateGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        final boolean worldGen = context.isWorldGen();
        final GenFeatureConfiguration configurationToPlace = configuration.get(GEN_FEATURE);
        if (configuration.getGenFeature().getRegistryName().equals(DTTrees.NULL)) return false;

        if (!(configuration.get(ONLY_WORLD_GEN) && !worldGen) &&
                //Negate the predicate test
                !configuration.get(BIOME_PREDICATE).test(context.biome())) {
            return configurationToPlace.generate(Type.POST_GENERATION, context);
        }

        return false;
    }
}
