package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.leaves.ScruffyLeavesProperties;
import maxhyper.dtaether.init.DTAetherClient;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.zepalesque.redux.client.particle.ReduxParticleTypes;

public class FieldsprootLeavesProperties extends ScruffyLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(FieldsprootLeavesProperties::new);

    public FieldsprootLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @OnlyIn(Dist.CLIENT)
    public static ParticleOptions particleFromPos(RandomSource rand, BlockPos pos) {
        ParticleOptions[] FIELDSPROOT_PARTICLES = new ParticleOptions[]{ReduxParticleTypes.FIELDSPROUT_PETALS_0.get(), ReduxParticleTypes.FIELDSPROUT_PETALS_1.get(),ReduxParticleTypes.FIELDSPROUT_PETALS_2.get(),ReduxParticleTypes.FIELDSPROUT_PETALS_3.get(),ReduxParticleTypes.FIELDSPROUT_PETALS_4.get(),ReduxParticleTypes.FIELDSPROUT_PETALS_5.get(),ReduxParticleTypes.FIELDSPROUT_PETALS_6.get()};
        if (rand.nextFloat() < 0.2F) return ReduxParticleTypes.FALLING_PRISMATIC_LEAVES.get();
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
        return FIELDSPROOT_PARTICLES[prism];
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
