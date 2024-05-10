//package maxhyper.dtaether.features;
//
//import com.aetherteam.aether.AetherTags;
//import com.aetherteam.aether.data.resources.registries.AetherConfiguredFeatures;
//import com.ferreusveritas.dynamictrees.init.DTConfigs;
//import com.mojang.serialization.Codec;
//import maxhyper.dtaether.init.DTAetherRegistries;
//import net.minecraft.core.Holder;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.levelgen.GenerationStep;
//import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
//import net.minecraftforge.common.world.BiomeModifier;
//import net.minecraftforge.common.world.ModifiableBiomeInfo;
//
//public class AddDynamicCrystalIslandBiomeModifier implements BiomeModifier {
//
//    @Override
//    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
//        if (DTConfigs.WORLD_GEN.get() && biome.is(AetherTags.Biomes.IS_AETHER)){
//            if (phase == Phase.ADD) {
//                BiomeGenerationSettingsBuilder generationSettings = builder.getGenerationSettings();
//                generationSettings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, DTAetherRegistries.DYNAMIC_CRYSTAL_ISLAND_PLACED_FEATURE.getHolder().orElseThrow());
//            }
//        }
//
//    }
//
//    @Override
//    public Codec<? extends BiomeModifier> codec() {
//        return DTAetherRegistries.ADD_DYNAMIC_CRYSTAL_ISLAND_BIOME_MODIFIER.get();
//    }
//}
