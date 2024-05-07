package maxhyper.dtaether.resources;

import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.google.gson.JsonElement;
import maxhyper.dtaether.DynamicTreesAether;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliersFamily(final ApplierRegistryEvent.Reload<Family, JsonElement> event) {
        registerFamilyAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliersCapProperties(final ApplierRegistryEvent.Reload<CapProperties, JsonElement> event) {
        registerCapPropertiesAppliers(event.getAppliers());
    }

    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
//        appliers.register("alternative_species", LamentSpecies.class, Species.class,
//                LamentSpecies::setAltSpecies)
//                .register("extra_soil_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
//                GenOnExtraSoilSpecies::setExtraSoil)
//                .register("soil_replacement_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
//                GenOnExtraSoilSpecies::setSoilReplacement);
    }

    public static void registerFamilyAppliers(PropertyAppliers<Family, JsonElement> appliers) {
//        appliers.register("primitive_imbued_log", ImbuedLogFamily.class, Block.class,
//                ImbuedLogFamily::setPrimitiveImbuedLog);
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

    @SubscribeEvent public static void registerAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) { registerSpeciesAppliers(event.getAppliers()); }
    @SubscribeEvent public static void registerAppliersFamily(final ApplierRegistryEvent.GatherData<Family, JsonElement> event) { registerFamilyAppliers(event.getAppliers()); }

}