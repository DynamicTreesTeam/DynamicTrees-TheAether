package maxhyper.dtaether.init;

import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.systems.growthlogic.GrowthLogicKit;
import com.dtteam.dynamictrees.systems.BranchConnectables;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.block.CommonVoxelShapes;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.blocks.*;
import maxhyper.dtaether.cells.DTAetherCellKits;
import maxhyper.dtaether.compat.CompatHandler;
import maxhyper.dtaether.genfeatures.DTAetherGenFeatures;
import maxhyper.dtaether.growthlogic.DTAetherGrowthLogicKits;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import maxhyper.dtaether.trees.ModDependentSpecies;
import maxhyper.dtaether.world.DynamicCrystalIslandFeature;
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

    public static final VoxelShape CLOUDCAP_CAP = Block.box(5.5D, 3.0D, 5.5D, 10.5D, 10.0D, 10.5D);
    public static final VoxelShape CLOUDCAP = Shapes.or(CommonVoxelShapes.SAPLING_TRUNK, CLOUDCAP_CAP);

    public static void setup(IEventBus modBus) {
        FEATURES.register(modBus);
        CompatHandler.setup();
        DTAetherGenFeatures.register(GenFeature.REGISTRY);
        DTAetherCellKits.register(CellKit.REGISTRY);
        DTAetherGrowthLogicKits.register(GrowthLogicKit.REGISTRY);

        Species.REGISTRY.registerType(DynamicTreesAether.location("mod_dependent"), ModDependentSpecies.TYPE);
        Family.REGISTRY.registerType(DynamicTreesAether.location("imbued_log"), ImbuedLogFamily.TYPE);
        SoilProperties.REGISTRY.registerType(DynamicTreesAether.location("alt_tint"), AltTintSoilProperties.TYPE);
        LeavesProperties.REGISTRY.registerType(DynamicTreesAether.location("particle"), ParticleLeavesProperties.TYPE);
        LeavesProperties.REGISTRY.registerType(DynamicTreesAether.location("fieldsproot"), FieldsprootLeavesProperties.TYPE);
        LeavesProperties.REGISTRY.registerType(DynamicTreesAether.location("scruffy_particle"), ScruffyParticleLeavesProperties.TYPE);
        LeavesProperties.REGISTRY.registerType(DynamicTreesAether.location("scruffy_snowy"), SnowyScruffyLeavesProperties.TYPE);
    }

    public static void setupBlocks() {
        CommonVoxelShapes.SHAPES.put(DynamicTreesAether.location("cloudcap").toString(), CLOUDCAP);
        if (ModList.get().isLoaded("aether_redux")) {
            ReduxOnlyFunctions.setupConnectables();
        }
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.dtteam.dynamictrees.event.RegistryEvent<GenFeature> event) {
        if (event.isEntryOfType(GenFeature.class)) {
            DTAetherGenFeatures.register(event.getRegistry());
        }
    }

    @SubscribeEvent
    public static void onCellKitRegistry (final com.dtteam.dynamictrees.event.RegistryEvent<CellKit> event) {
        if (event.isEntryOfType(CellKit.class)) {
            DTAetherCellKits.register(event.getRegistry());
        }
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.dtteam.dynamictrees.event.RegistryEvent<GrowthLogicKit> event) {
        if (event.isEntryOfType(GrowthLogicKit.class)) {
            DTAetherGrowthLogicKits.register(event.getRegistry());
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
            event.registerType(DynamicTreesAether.location("particle"), ParticleLeavesProperties.TYPE);
            event.registerType(DynamicTreesAether.location("fieldsproot"), FieldsprootLeavesProperties.TYPE);
            event.registerType(DynamicTreesAether.location("scruffy_particle"), ScruffyParticleLeavesProperties.TYPE);
            event.registerType(DynamicTreesAether.location("scruffy_snowy"), SnowyScruffyLeavesProperties.TYPE);
        }
    }

}
