package maxhyper.dtaether.init;

import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.api.worldgen.FeatureCanceller;
import com.dtteam.dynamictrees.systems.BranchConnectables;
import maxhyper.dtaether.cancellers.AetherTreeFeatureCanceller;
import maxhyper.dtaether.DynamicTreesAether;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ReduxOnlyFunctions {

    public static void setupConnectables() {
        makeConnectable("jellyshroom_jelly_block");
        makeConnectable("cloudcap_spores");
    }

    public static final FeatureCanceller REDUX_CLOUDCAP_CANCELLER =
            new AetherTreeFeatureCanceller<>(DynamicTreesAether.location("cloudcap"), FeatureConfiguration.class);
    public static final FeatureCanceller REDUX_JELLYSHROOM_CANCELLER =
            new AetherTreeFeatureCanceller<>(DynamicTreesAether.location("jellyshroom"), FeatureConfiguration.class);
    public static void registerFeatureCancellers(final RegistryEvent<FeatureCanceller> event){
        event.getRegistry().registerAll(REDUX_CLOUDCAP_CANCELLER, REDUX_JELLYSHROOM_CANCELLER);
    }

    public static void registerFeatureCancellersDirectly() {
        FeatureCanceller.REGISTRY.registerAll(REDUX_CLOUDCAP_CANCELLER, REDUX_JELLYSHROOM_CANCELLER);
    }

    private static void makeConnectable(String path) {
        Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("aether_redux", path));
        BranchConnectables.makeBlockConnectable(block, (state, level, pos, side) -> side.getAxis() != Direction.Axis.Y ? 2 : 0);
    }

}
