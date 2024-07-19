package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.leaves.ScruffyLeavesProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ScruffyParticleLeavesProperties extends ScruffyLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(ScruffyParticleLeavesProperties::new);

    public ScruffyParticleLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new DynamicLeavesBlock(this, properties){
            public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
                getPrimitiveLeavesBlock().ifPresent((b)->b.animateTick(state,level,pos,random));
            }
        };
    }
}
