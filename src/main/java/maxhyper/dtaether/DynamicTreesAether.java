package maxhyper.dtaether;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import com.ferreusveritas.dynamictrees.resources.Resources;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import maxhyper.dtaether.data.DTAetherExtraLang;
import maxhyper.dtaether.init.DTAetherClient;
import maxhyper.dtaether.init.DTAetherPlusRegistries;
import maxhyper.dtaether.init.DTAetherRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTreesAether.MOD_ID)
public class DynamicTreesAether
{
    public static final String MOD_ID = "dtaether";

    public DynamicTreesAether() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::gatherData);

        if (ModList.get().isLoaded("dynamictreesplus")){
            eventBus.register(DTAetherPlusRegistries.class);
        }

        RegistryHandler.setup(MOD_ID);
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
        return new ResourceLocation(MOD_ID, path);
    }

}
