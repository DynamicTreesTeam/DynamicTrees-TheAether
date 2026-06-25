package maxhyper.dtaether.compat;

public class CompatHandler {
    public static addonBlockStateProperties blockStateProperties;

    public static void setup(){
        if (net.neoforged.fml.ModList.get().isLoaded("aether_redux")) {
            blockStateProperties = new ReduxBlockStateProperties();
        }
    }

}
