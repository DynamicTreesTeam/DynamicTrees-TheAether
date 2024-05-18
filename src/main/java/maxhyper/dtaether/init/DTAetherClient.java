package maxhyper.dtaether.init;

import com.ferreusveritas.dynamictrees.client.BlockColorMultipliers;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;

import java.util.stream.IntStream;

public class DTAetherClient {

    public static final PerlinNoise PERLIN = PerlinNoise.create(new XoroshiroRandomSource(2743L), IntStream.of(0));

    public static void setup() {
        registerJsonColorMultipliers();
    }

    private static final int[] FIELDSPROOT_COLORS = new int[]{0x96e9e2,0xa7e3e6,0xb9ddea,0xcad8ee,0xdcd2f3,0xedccf7,0xedccf7};
    private static void registerJsonColorMultipliers() {
        BlockColorMultipliers.register("dtaether:fieldsproot", (state, level, pos, tintIndex) -> {
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
    }

    public static int lerpInt(float delta, int start, int end) {
        return start + Mth.floor(delta * (float)(end - start));
    }

}
