package maxhyper.dtaether.init;

import com.dtteam.dynamictrees.api.worldgen.FeatureCanceller;
import com.dtteam.dynamictrees.event.RegistryEvent;
import maxhyper.dtaether.DynamicTreesAether;
import maxhyper.dtaether.cancellers.AetherTreeFeatureCanceller;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class DeepAetherOnlyFunctions {

    private DeepAetherOnlyFunctions() {
    }

    public static final FeatureCanceller AERCLOUD_MUSHROOM_CANCELLER =
            new AetherTreeFeatureCanceller<>(
                    DynamicTreesAether.location("aercloud_mushroom"),
                    FeatureConfiguration.class
            );

    public static void registerFeatureCancellers(final RegistryEvent<FeatureCanceller> event) {
        event.getRegistry().register(AERCLOUD_MUSHROOM_CANCELLER);
    }

    public static void registerFeatureCancellersDirectly() {
        FeatureCanceller.REGISTRY.register(AERCLOUD_MUSHROOM_CANCELLER);
    }

}
