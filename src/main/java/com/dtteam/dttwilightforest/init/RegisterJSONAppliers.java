package com.dtteam.dttwilightforest.init;

import com.dtteam.dynamictrees.event.ApplierRegistryEvent;
import com.dtteam.dynamictrees.deserialization.PropertyAppliers;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import com.dtteam.dttwilightforest.DynamicTreesTheTwilightForest;
import com.dtteam.dttwilightforest.trees.MagicFamily;
import com.dtteam.dttwilightforest.trees.TwilightMangroveFamily;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = DynamicTreesTheTwilightForest.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersFamily(final ApplierRegistryEvent.Reload<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }
    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    public static void registerFamilyAppliers(PropertyAppliers<Family, JsonElement> appliers) {
        appliers.register("primitive_core_log", MagicFamily.class, Block.class, MagicFamily::setPrimitiveCoreLog)
                .register("primitive_grassy_root", TwilightMangroveFamily.class, Block.class, TwilightMangroveFamily::setPrimitiveRootsGrassy)
                .register("grass_spread_required_light", TwilightMangroveFamily.class, Integer.class, TwilightMangroveFamily::setGrassSpreadRequiredLight);
    }
    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
//        appliers.register("root_soil", MangroveSpecies.class, SoilProperties.class, MangroveSpecies::setDefaultSoil)
//                .register("worldgen_height_offset", MangroveSpecies.class, Integer.class, MangroveSpecies::setWorldgenHeightOffset);
    }

    @SubscribeEvent
    public static void registerAppliersSpeciesData(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliersFamilyData(final ApplierRegistryEvent.GatherData<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }

}
