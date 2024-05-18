package maxhyper.dtaether.init;

import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.blocks.AltTintSoilProperties;
import maxhyper.dtaether.blocks.ParticleLeavesProperties;
import maxhyper.dtaether.blocks.SnowyScruffyLeavesProperties;
import maxhyper.dtaether.cells.DTAetherCellKits;
import maxhyper.dtaether.features.DynamicCrystalIslandFeature;
import maxhyper.dtaether.genfeatures.DTAetherGenFeatures;
import maxhyper.dtaether.growthlogic.DTAetherGrowthLogicKits;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import maxhyper.dtaether.trees.ModDependentSpecies;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTAetherRegistries {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, DynamicTreesAether.MOD_ID);
    public static final RegistryObject<DynamicCrystalIslandFeature> DYNAMIC_CRYSTAL_ISLAND_FEATURE = FEATURES.register("crystal_island", DynamicCrystalIslandFeature::new);

    public static void setup(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    public static void setupBlocks() {
        setUpSoils();
        setupConnectables();
    }

    private static void setUpSoils() {

    }

    private static void setupConnectables() {

    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        DTAetherGenFeatures.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onCellKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<CellKit> event) {
        DTAetherCellKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        DTAetherGrowthLogicKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
        event.registerType(DynamicTreesAether.location("mod_dependent"), ModDependentSpecies.TYPE);
    }
    
    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
        event.registerType(DynamicTreesAether.location("imbued_log"), ImbuedLogFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerSoilPropertiesTypes (final TypeRegistryEvent<SoilProperties> event) {
        event.registerType(DynamicTreesAether.location("alt_tint"), AltTintSoilProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(DynamicTreesAether.location("particle"), ParticleLeavesProperties.TYPE);
        event.registerType(DynamicTreesAether.location("scruffy_snowy"), SnowyScruffyLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerFruitTypes(final TypeRegistryEvent<Fruit> event) {

    }

//    public static final FeatureCanceller BYG_TREE_CANCELLER = new BYGTreeFeatureCanceller<>(DynamicTreesAether.location("tree"), TreeFromStructureNBTConfig.class);
//    public static final FeatureCanceller BYG_FUNGUS_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesAether.location("fungus"), BYGMushroomConfig.class);
//    public static final FeatureCanceller GIANT_FLOWER_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesAether.location("giant_flower"), GiantFlowerConfig.class);

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {
//        event.getRegistry().registerAll(BYG_TREE_CANCELLER);
//        event.getRegistry().registerAll(BYG_FUNGUS_CANCELLER);
//        event.getRegistry().registerAll(GIANT_FLOWER_CANCELLER);
    }

}
