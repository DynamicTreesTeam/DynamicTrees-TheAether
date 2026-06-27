package com.dtteam.dtaether.init;

import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.systems.BranchConnectables;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.block.CommonVoxelShapes;
import com.dtteam.dtaether.DynamicTreesAether;
import com.dtteam.dtaether.blocks.*;
import com.dtteam.dtaether.genfeatures.DTAetherGenFeatures;
import com.dtteam.dtaether.trees.ImbuedLogFamily;
import com.dtteam.dtaether.trees.ModDependentSpecies;
import com.dtteam.dtaether.world.DynamicCrystalIslandFeature;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class DTAetherRegistries {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, DynamicTreesAether.MOD_ID);
    public static final DeferredHolder<Feature<?>, DynamicCrystalIslandFeature> DYNAMIC_CRYSTAL_ISLAND_FEATURE =
            FEATURES.register("crystal_island", DynamicCrystalIslandFeature::new);

    public static void setup(IEventBus modBus) {
        FEATURES.register(modBus);
        DTAetherGenFeatures.register(GenFeature.REGISTRY);
        Species.REGISTRY.registerType(DynamicTreesAether.location("mod_dependent"), ModDependentSpecies.TYPE);
        Family.REGISTRY.registerType(DynamicTreesAether.location("imbued_log"), ImbuedLogFamily.TYPE);
        SoilProperties.REGISTRY.registerType(DynamicTreesAether.location("alt_tint"), AltTintSoilProperties.TYPE);
    }

    public static void setupBlocks() {
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.dtteam.dynamictrees.event.RegistryEvent<GenFeature> event) {
        if (event.isEntryOfType(GenFeature.class)) {
            DTAetherGenFeatures.register(event.getRegistry());
        }
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        if (event.isEntryOfType(Species.class)) {
            event.registerType(DynamicTreesAether.location("mod_dependent"), ModDependentSpecies.TYPE);
        }
    }
    
    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        if (event.isEntryOfType(Family.class)) {
            event.registerType(DynamicTreesAether.location("imbued_log"), ImbuedLogFamily.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerSoilPropertiesTypes (final TypeRegistryEvent<SoilProperties> event) {
        if (event.isEntryOfType(SoilProperties.class)) {
            event.registerType(DynamicTreesAether.location("alt_tint"), AltTintSoilProperties.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        if (event.isEntryOfType(LeavesProperties.class)) {
        }
    }

}
