package maxhyper.dtaether.resources;

import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.google.gson.JsonElement;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    //SPECIES
    @SubscribeEvent
    public static void registerReloadAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }
    @SubscribeEvent public static void registerDataAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }
    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
//        appliers.register("alternative_species", LamentSpecies.class, Species.class,
//                LamentSpecies::setAltSpecies)
//                .register("extra_soil_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
//                GenOnExtraSoilSpecies::setExtraSoil)
//                .register("soil_replacement_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
//                GenOnExtraSoilSpecies::setSoilReplacement);
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
                        ImbuedLogFamily::setImbuedBranchName);
    }

    //MUSHROOM
    @SubscribeEvent
    public static void registerReloadAppliersCapProperties(final ApplierRegistryEvent.Reload<CapProperties, JsonElement> event) {
        registerCapPropertiesAppliers(event.getAppliers());
    }
    public static void registerCapPropertiesAppliers(PropertyAppliers<CapProperties, JsonElement> appliers) {
//        appliers.register("shroomlight_block", WartyCapProperties.class, Block.class,
//                WartyCapProperties::setShroomlightBlock)
//                .register("shroomlight_above_place_chance", WartyCapProperties.class, Float.class,
//                        WartyCapProperties::setShroomlightUpChance)
//                .register("shroomlight_below_place_chance", WartyCapProperties.class, Float.class,
//                        WartyCapProperties::setShroomlightDownChance)
//                .register("shroomlight_requires_support", WartyCapProperties.class, Boolean.class,
//                        WartyCapProperties::setShroomlightRequireSupport);
    }

}