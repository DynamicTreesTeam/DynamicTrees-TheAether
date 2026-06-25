package maxhyper.dtaether.init;

import com.dtteam.dynamictrees.client.BlockColorMultipliers;
import maxhyper.dtaether.DynamicTreesAether;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.stream.IntStream;

@EventBusSubscriber(modid = DynamicTreesAether.MOD_ID, value = Dist.CLIENT)
public class DTAetherClient {

    public static void setup(){
//        ItemBlockRenderTypes.setRenderLayer(jellyCap, RenderType.translucent());
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerColorHandlersEvent(RegisterColorHandlersEvent.Block event){
        registerJsonColorMultipliers();
    }

    public static final PerlinNoise PERLIN = PerlinNoise.create(new XoroshiroRandomSource(2743L), IntStream.of(0));
    private static final int[] FIELDSPROOT_COLORS = new int[]{0x96e9e2,0xa7e3e6,0xb9ddea,0xcad8ee,0xdcd2f3,0xedccf7,0xedccf7};
    @OnlyIn(Dist.CLIENT)
    private static void registerJsonColorMultipliers() {
        try {
            BlockColorMultipliers.register(DynamicTreesAether.location("fieldsproot"), (state, level, pos, tintIndex) -> {
                double scale = 0.1;
                int prism = 0;
                if (pos != null){
                    double x = pos.getX() * scale;
                    double y = pos.getY() * scale;
                    double z = pos.getZ() * scale;
                    double noiseVal = PERLIN.getValue(x, y, z);
                    double clamped = Mth.clamp(noiseVal, -0.5, 0.5);
                    prism = lerpInt((float)clamped + 0.5F, 0, 6);
                }
                return FIELDSPROOT_COLORS[prism];
            });
        } catch (Exception e){
            System.out.println("error registering fieldsproot color handler");
        }
    }

    public static int lerpInt(float delta, int start, int end) {
        return start + Mth.floor(delta * (float)(end - start));
    }
}
