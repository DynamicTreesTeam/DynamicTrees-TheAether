package maxhyper.dtaether.resources;

import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.deserialisation.JsonHelper;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.blocks.ParticleLeavesProperties;
import maxhyper.dtaether.trees.ModDependentSpecies;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = DynamicTreesAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
        appliers.register("variant_properties", ModDependentSpecies.class, JsonObject.class,
                ModDependentSpecies::setLoadVariantProperties);
    }
    public static void registerReloadSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
       appliers.register("variant_properties", ModDependentSpecies.class, JsonObject.class,
                ModDependentSpecies::setReloadVariantProperties);
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
        appliers.register("particle_type", ParticleLeavesProperties.class, ResourceLocation.class, ParticleLeavesProperties::setParticleResLoc);
    }

}
