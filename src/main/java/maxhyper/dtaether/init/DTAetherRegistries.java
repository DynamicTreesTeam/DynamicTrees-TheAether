package maxhyper.dtaether.init;

import com.ferreusveritas.dynamictrees.api.cell.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CommonVoxelShapes;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.blocks.*;
import maxhyper.dtaether.cells.DTAetherCellKits;
import maxhyper.dtaether.compat.CompatHandler;
import maxhyper.dtaether.genfeatures.DTAetherGenFeatures;
import maxhyper.dtaether.growthlogic.DTAetherGrowthLogicKits;
import maxhyper.dtaether.trees.ImbuedLogFamily;
import maxhyper.dtaether.trees.ModDependentSpecies;
import maxhyper.dtaether.world.DynamicCrystalIslandFeature;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
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

    public static final VoxelShape CLOUDCAP_CAP = Block.box(5.5D, 3.0D, 5.5D, 10.5D, 10.0D, 10.5D);
    public static final VoxelShape CLOUDCAP = Shapes.or(CommonVoxelShapes.MUSHROOM_STEM, CLOUDCAP_CAP);

    public static void setup(IEventBus modBus) {
        FEATURES.register(modBus);
        CompatHandler.setup();
    }

    public static void setupBlocks() {
        CommonVoxelShapes.SHAPES.put(DynamicTreesAether.location("cloudcap").toString(), CLOUDCAP);
        setupConnectables();
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
        event.registerType(DynamicTreesAether.location("fieldsproot"), FieldsprootLeavesProperties.TYPE);
        event.registerType(DynamicTreesAether.location("particle"), ParticleLeavesProperties.TYPE);
        event.registerType(DynamicTreesAether.location("scruffy_particle"), ScruffyParticleLeavesProperties.TYPE);
        event.registerType(DynamicTreesAether.location("scruffy_snowy"), SnowyScruffyLeavesProperties.TYPE);
    }

}
