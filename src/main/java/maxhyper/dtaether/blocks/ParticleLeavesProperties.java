package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleLeavesProperties extends LeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(ParticleLeavesProperties::new);

    private ResourceLocation particleResLoc;
    private SimpleParticleType particleType;
    protected boolean valid = true;

    public void setParticleResLoc(ResourceLocation particleResLoc) {
        this.particleResLoc = particleResLoc;
    }

    public ParticleOptions getParticleOptions(){
        if (particleResLoc == null) valid = false;
        if (valid && particleType == null){
            ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(particleResLoc);
            if (type instanceof SimpleParticleType simpleType)
                particleType = simpleType;
            else {
                valid = false;
                //System.out.printf("Error loading particle type %s for leaves properties %s. Particle must be of SimpleParticleType.", type, this);
            }
        }
        return particleType;
    }

    public ParticleLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new DynamicLeavesBlock(this, properties){
            public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                super.animateTick(state, level, pos, random);
                if (valid && level.isClientSide() && random.nextInt(10) == 0) {
                    for(int i = 0; i < 15; ++i) {
                        double x = (double)pos.getX() + ((double)random.nextFloat() - 0.5) * 8.0;
                        double y = (double)pos.getY() + ((double)random.nextFloat() - 0.5) * 8.0;
                        double z = (double)pos.getZ() + ((double)random.nextFloat() - 0.5) * 8.0;
                        double dx = ((double)random.nextFloat() - 0.5) * 0.5;
                        double dy = ((double)random.nextFloat() - 0.5) * 0.5;
                        double dz = ((double)random.nextFloat() - 0.5) * 0.5;
                        level.addParticle(getParticleOptions(), x, y, z, dx, dy, dz);
                    }
                }

            }
        };
    }
}
