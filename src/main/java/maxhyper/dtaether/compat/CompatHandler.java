package maxhyper.dtaether.compat;

import net.minecraftforge.fml.ModList;

public class CompatHandler {
    public static addonBlockStateProperties blockStateProperties;

    public static void setup(){
        if (ModList.get().isLoaded("aether_redux")){
            blockStateProperties = new ReduxBlockStateProperties();
        }
    }

}
