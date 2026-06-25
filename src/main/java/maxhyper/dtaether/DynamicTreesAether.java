package maxhyper.dtaether;

import com.dtteam.dynamictrees.data.GatherDataHelper;
import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.soil.SoilProperties;
import com.dtteam.dynamictrees.treepack.Resources;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.pod.Pod;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictreesplus.block.mushroom.CapProperties;
import maxhyper.dtaether.data.DTAetherExtraLang;
import maxhyper.dtaether.init.DTAetherClient;
import maxhyper.dtaether.init.DTAetherPlusRegistries;
import maxhyper.dtaether.init.DTAetherRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(DynamicTreesAether.MOD_ID)
public class DynamicTreesAether
{
    public static final String MOD_ID = "dtaether";

    public DynamicTreesAether(IEventBus eventBus) {
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::gatherData);

        if (ModList.get().isLoaded("dynamictreesplus")){
            DTAetherPlusRegistries.setup();
        }

        NeoForgeRegistryHandler.setup(MOD_ID, eventBus);
        DTAetherRegistries.setup(eventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        DTAetherRegistries.setupBlocks();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTAetherClient.setup();
    }

    private void gatherData(final GatherDataEvent event) {
        Resources.MANAGER.gatherData();
        GatherDataHelper.addLangGenerator(MOD_ID, new DTAetherExtraLang());
        GatherDataHelper.gatherAllData(
                MOD_ID,
                event,
                SoilProperties.REGISTRY,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY,
                Fruit.REGISTRY,
                Pod.REGISTRY,
                CapProperties.REGISTRY
        );
    }

    public static ResourceLocation location(final String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
