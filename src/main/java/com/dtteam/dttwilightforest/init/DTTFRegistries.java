package com.dtteam.dttwilightforest.init;

import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.api.worldgen.FeatureCanceller;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dttwilightforest.DynamicTreesTheTwilightForest;
import com.dtteam.dttwilightforest.canceller.SimpleFeatureCanceller;
import com.dtteam.dttwilightforest.cellkits.DTTFCellKits;
import com.dtteam.dttwilightforest.genfeatures.DTTFGenFeatures;
import com.dtteam.dttwilightforest.growthlogic.DTTFGrowthLogicKits;
import com.dtteam.dttwilightforest.trees.GigaSpruceSpecies;
import com.dtteam.dttwilightforest.trees.MagicFamily;
import com.dtteam.dttwilightforest.trees.TwilightMangroveFamily;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import twilightforest.world.components.feature.config.TFTreeFeatureConfig;

@EventBusSubscriber(modid = DynamicTreesTheTwilightForest.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DTTFRegistries {

    public static void setup() {
    }

    @SubscribeEvent
    public static void registerFamilyTypes(final TypeRegistryEvent<Family> event) {
        if (!event.isEntryOfType(Family.class)) return;
        event.registerType(DynamicTreesTheTwilightForest.location("magic"), MagicFamily.TYPE);
        event.registerType(DynamicTreesTheTwilightForest.location("mangrove"), TwilightMangroveFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerSpeciesTypes(final TypeRegistryEvent<Species> event) {
        if (!event.isEntryOfType(Species.class)) return;
        event.registerType(DynamicTreesTheTwilightForest.location("giga_spruce"), GigaSpruceSpecies.TYPE);
    }

    @SubscribeEvent
    public static void registerSoilPropertiesTypes(final TypeRegistryEvent<SoilProperties> event) {
        if (!event.isEntryOfType(SoilProperties.class)) return;
        //event.registerType(DynamicTreesTheTwilightForest.location("uberous_soil"), UberousSoilProperties.TYPE);
    }

    public static final FeatureCanceller TREE_CANCELLER = new SimpleFeatureCanceller<>(DynamicTreesTheTwilightForest.location("all_trees"), TFTreeFeatureConfig.class);
    public static final FeatureCanceller MUSHROOM_CANCELLER = new SimpleFeatureCanceller<>(DynamicTreesTheTwilightForest.location("all_mushrooms"), HugeMushroomFeatureConfiguration.class);

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final RegistryEvent<FeatureCanceller> event) {
        if (!event.isEntryOfType(FeatureCanceller.class)) return;
        event.getRegistry().registerAll(TREE_CANCELLER, MUSHROOM_CANCELLER);
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry(final RegistryEvent<GrowthLogicKit> event) {
        if (!event.isEntryOfType(GrowthLogicKit.class)) return;
        DTTFGrowthLogicKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onCellKitRegistry(final RegistryEvent<CellKit> event) {
        if (!event.isEntryOfType(CellKit.class)) return;
        DTTFCellKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry(final RegistryEvent<GenFeature> event) {
        if (!event.isEntryOfType(GenFeature.class)) return;
        DTTFGenFeatures.register(event.getRegistry());
    }

}
