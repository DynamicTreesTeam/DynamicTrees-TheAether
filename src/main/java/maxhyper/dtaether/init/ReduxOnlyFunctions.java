package maxhyper.dtaether.init;

import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.systems.BranchConnectables;
import com.ferreusveritas.dynamictrees.worldgen.featurecancellation.TreeFeatureCanceller;
import maxhyper.dtaether.DynamicTreesAether;
import net.minecraft.core.Direction;
import net.zepalesque.redux.block.ReduxBlocks;
import net.zepalesque.redux.world.feature.CloudcapFeature;
import net.zepalesque.redux.world.feature.JellyshroomFeature;

public class ReduxOnlyFunctions {

    public static void setupConnectables() {
        BranchConnectables.makeBlockConnectable(ReduxBlocks.JELLYSHROOM_JELLY_BLOCK.get(), (state, level, pos, side) -> side.getAxis() != Direction.Axis.Y ? 2 : 0);
        BranchConnectables.makeBlockConnectable(ReduxBlocks.CLOUDCAP_SPORES.get(), (state, level, pos, side) -> side.getAxis() != Direction.Axis.Y ? 2 : 0);
    }

    public static final FeatureCanceller REDUX_CLOUDCAP_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesAether.location("cloudcap"), CloudcapFeature.Config.class);
    public static final FeatureCanceller REDUX_JELLYSHROOM_CANCELLER = new TreeFeatureCanceller<>(DynamicTreesAether.location("jellyshroom"), JellyshroomFeature.Config.class);
    public static void registerFeatureCancellers(final RegistryEvent<FeatureCanceller> event){
        event.getRegistry().registerAll(REDUX_CLOUDCAP_CANCELLER, REDUX_JELLYSHROOM_CANCELLER);
    }

}
