package maxhyper.dtaether.blocks;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.block.leaves.ScruffyLeavesProperties;
import maxhyper.dtaether.init.DTAetherClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class FieldsprootLeavesProperties extends ScruffyLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(FieldsprootLeavesProperties::new);

    public FieldsprootLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @OnlyIn(Dist.CLIENT)
    public static ParticleOptions particleFromPos(RandomSource rand, BlockPos pos) {
        ParticleOptions[] fieldsprootParticles = new ParticleOptions[7];
        for (int i = 0; i < fieldsprootParticles.length; i++) {
            fieldsprootParticles[i] = getParticle("fieldsprout_petals_" + i, ParticleTypes.CHERRY_LEAVES);
        }
        if (rand.nextFloat() < 0.2F) {
            return getParticle("falling_prismatic_leaves", ParticleTypes.CHERRY_LEAVES);
        }
        double scale = 0.1;
        int prism = 0;
        if (pos != null){
            double x = pos.getX() * scale;
            double y = pos.getY() * scale;
            double z = pos.getZ() * scale;
            double noiseVal = DTAetherClient.PERLIN.getValue(x, y, z);
            double clamped = Mth.clamp(noiseVal, -0.5, 0.5);
            prism = DTAetherClient.lerpInt((float)clamped + 0.5F, 0, 6);
        }
        return fieldsprootParticles[prism];
    }

    private static ParticleOptions getParticle(String path, SimpleParticleType fallback) {
        var particle = BuiltInRegistries.PARTICLE_TYPE.get(ResourceLocation.fromNamespaceAndPath("aether_redux", path));
        return particle instanceof SimpleParticleType simpleParticle ? simpleParticle : fallback;
    }


    @Override
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new DynamicLeavesBlock(this, properties){
            public void animateTick(BlockState block, Level world, BlockPos position, RandomSource rand) {
                super.animateTick(block, world, position, rand);
                if (rand.nextInt(15) == 0) {
                    BlockPos blockpos = position.below();
                    BlockState blockstate = world.getBlockState(blockpos);
                    if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(world, blockpos, Direction.UP)) {
                        ParticleUtils.spawnParticleBelow(world, position, rand, particleFromPos(rand, position));
                    }
                }
            }
        };
    }
}
