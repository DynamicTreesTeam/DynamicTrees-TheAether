package com.dtteam.dtaether.resources;

import com.dtteam.dynamictrees.event.ApplierRegistryEvent;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.deserialization.JsonHelper;
import com.dtteam.dynamictrees.deserialization.PropertyAppliers;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.dtteam.dtaether.DynamicTreesAether;
import com.dtteam.dtaether.trees.ModDependentSpecies;
import com.dtteam.dtaether.trees.ImbuedLogFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Objects;

@EventBusSubscriber(modid = DynamicTreesAether.MOD_ID)
public final class RegisterJSONAppliers {

    //SPECIES
    @SubscribeEvent
    public static void registerLoadAppliersSpecies(final ApplierRegistryEvent.Load<Species, JsonElement> event) {
        registerLoadSpeciesAppliers(event.getAppliers());
    }
    @SubscribeEvent
    public static void registerReloadAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerReloadSpeciesAppliers(event.getAppliers());
    }
    @SubscribeEvent public static void registerDataAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) {
        //registerLoadSpeciesAppliers(event.getAppliers());
    }
    public static void registerLoadSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.registerArrayApplier("variant_properties", ModDependentSpecies.class, JsonObject.class, ModDependentSpecies::setLoadVariantProperties);
    }
    public static void registerReloadSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
       appliers.registerArrayApplier("variant_properties", ModDependentSpecies.class, JsonObject.class,
               (species,jsonObject)->Species.REGISTRY.runOnNextLock(()->species.setReloadVariantProperties(jsonObject)));
    }

    //FAMILY
    @SubscribeEvent
    public static void registerSetupAppliersFamily(final ApplierRegistryEvent.Setup<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }
//    @SubscribeEvent
//    public static void registerLoadAppliersFamily(final ApplierRegistryEvent.Load<Family, JsonElement> event) {
//        event.getAppliers().register("imbued_branch_name", ImbuedLogFamily.class, ResourceLocation.class,
//                ImbuedLogFamily::setImbuedBranchName);
//    }
    @SubscribeEvent public static void registerDataAppliersFamily(final ApplierRegistryEvent.GatherData<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }
    public static void registerFamilyAppliers(PropertyAppliers<Family, JsonElement> appliers) {
        appliers.register("primitive_imbued_log", ImbuedLogFamily.class, Block.class,
                        ImbuedLogFamily::setPrimitiveImbuedLog)
                .register("imbued_drop", ImbuedLogFamily.class, Item.class,
                        ImbuedLogFamily::setImbuedDropItem)
                .register("imbued_branch_name", ImbuedLogFamily.class, ResourceLocation.class,
                        ImbuedLogFamily::setImbuedBranchName)
                .register("strip_loot_location", ImbuedLogFamily.class, ResourceLocation.class,
                        ImbuedLogFamily::setStripLootLocation);
    }

    //LEAVES
    @SubscribeEvent
    public static void registerLoadAppliersLeavesProperties(final ApplierRegistryEvent.Load<LeavesProperties, JsonElement> event) {
        registerLeavesPropertiesAppliers(event.getAppliers());
    }

    public static void registerLeavesPropertiesAppliers(PropertyAppliers<LeavesProperties, JsonElement> appliers) {
        //appliers.register("particle_type", ParticleLeavesProperties.class, ResourceLocation.class, ParticleLeavesProperties::setParticleResLoc);
    }

}
