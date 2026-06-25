package maxhyper.dtaether.init;

import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.api.worldgen.FeatureCanceller;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import com.dtteam.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.blocks.DropBlocksCapProperties;
import maxhyper.dtaether.blocks.CloudcapCapProperties;
import maxhyper.dtaether.blocks.JellyshroomCapProperties;
import maxhyper.dtaether.mushroomlogic.DTAetherMushroomShapeKits;
import maxhyper.dtaether.trees.DropLogsMushroomFamily;
import maxhyper.dtaether.trees.DropLogsMushroomSpecies;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;

public class DTAetherPlusRegistries {

    public static void setup() {
        DTAetherMushroomShapeKits.register(MushroomShapeKit.REGISTRY);
        CapProperties.REGISTRY.registerType(DynamicTreesAether.location("drop_blocks_cap"), DropBlocksCapProperties.TYPE);
        Species.REGISTRY.registerType(DynamicTreesAether.location("drop_logs_mushroom"), DropLogsMushroomSpecies.TYPE);
        Family.REGISTRY.registerType(DynamicTreesAether.location("drop_logs_mushroom"), DropLogsMushroomFamily.TYPE);

        if (ModList.get().isLoaded("aether_redux")) {
            CapProperties.REGISTRY.registerType(DynamicTreesAether.location("jellyshroom"), JellyshroomCapProperties.TYPE);
            CapProperties.REGISTRY.registerType(DynamicTreesAether.location("cloudcap"), CloudcapCapProperties.TYPE);
            ReduxOnlyFunctions.registerFeatureCancellersDirectly();
        }
        if (ModList.get().isLoaded("deep_aether")) {
            DeepAetherOnlyFunctions.registerFeatureCancellersDirectly();
        }
    }

    @SubscribeEvent
    public static void onMushroomShapeKitRegistry(RegistryEvent<MushroomShapeKit> event) {
        if (event.isEntryOfType(MushroomShapeKit.class)) {
            DTAetherMushroomShapeKits.register(event.getRegistry());
        }
    }

    @SubscribeEvent
    public static void registerCapTypes (final TypeRegistryEvent<CapProperties> event) {
        if (event.isEntryOfType(CapProperties.class)) {
            event.registerType(DynamicTreesAether.location("drop_blocks_cap"), DropBlocksCapProperties.TYPE);
            if (ModList.get().isLoaded("aether_redux")) {
                event.registerType(DynamicTreesAether.location("jellyshroom"), JellyshroomCapProperties.TYPE);
                event.registerType(DynamicTreesAether.location("cloudcap"), CloudcapCapProperties.TYPE);
            }
        }
    }

    @SubscribeEvent
    public static void registerSpeciesTypes(final TypeRegistryEvent<Species> event) {
        if (event.isEntryOfType(Species.class)) {
            event.registerType(DynamicTreesAether.location("drop_logs_mushroom"), DropLogsMushroomSpecies.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerFamilyTypes(final TypeRegistryEvent<Family> event) {
        if (event.isEntryOfType(Family.class)) {
            event.registerType(DynamicTreesAether.location("drop_logs_mushroom"), DropLogsMushroomFamily.TYPE);
        }
    }

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final RegistryEvent<FeatureCanceller> event) {
        if (event.isEntryOfType(FeatureCanceller.class)) {
            if (ModList.get().isLoaded("aether_redux")) {
                ReduxOnlyFunctions.registerFeatureCancellers(event);
            }
            if (ModList.get().isLoaded("deep_aether")) {
                DeepAetherOnlyFunctions.registerFeatureCancellers(event);
            }
        }
    }

}
