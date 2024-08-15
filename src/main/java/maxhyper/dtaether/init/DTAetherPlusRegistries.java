package maxhyper.dtaether.init;

import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.shapekits.MushroomShapeKit;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.blocks.DropBlocksCapProperties;
import maxhyper.dtaether.blocks.JellyshroomCapProperties;
import maxhyper.dtaether.mushroomlogic.DTAetherMushroomShapeKits;
import maxhyper.dtaether.trees.DropLogsMushroomFamily;
import maxhyper.dtaether.trees.DropLogsMushroomSpecies;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class DTAetherPlusRegistries {

    @SubscribeEvent
    public static void onMushroomShapeKitRegistry(RegistryEvent<MushroomShapeKit> event) {
        DTAetherMushroomShapeKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(DynamicTreesAether.location("drop_logs_mushroom"), DropLogsMushroomSpecies.TYPE);
    }

    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        event.registerType(DynamicTreesAether.location("drop_logs_mushroom"), DropLogsMushroomFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerCapTypes (final TypeRegistryEvent<CapProperties> event) {
        event.registerType(DynamicTreesAether.location("drop_blocks_cap"), DropBlocksCapProperties.TYPE);
        if (ModList.get().isLoaded("aether_redux")){
            event.registerType(DynamicTreesAether.location("jellyshroom"), JellyshroomCapProperties.TYPE);
        }
    }

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {
        if (ModList.get().isLoaded("aether_redux")){
            ReduxOnlyFunctions.registerFeatureCancellers(event);
        }
    }

}
