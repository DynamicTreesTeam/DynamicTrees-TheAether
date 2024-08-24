package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictreesplus.block.mushroom.CapProperties;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapBlock;
import com.ferreusveritas.dynamictreesplus.block.mushroom.DynamicCapCenterBlock;
import com.ferreusveritas.dynamictreesplus.systems.mushroomlogic.context.MushroomCapContext;
import com.ferreusveritas.dynamictreesplus.tree.HugeMushroomSpecies;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.zepalesque.redux.block.ReduxBlocks;

import java.util.HashSet;
import java.util.Set;

public class CloudcapCapProperties extends DropBlocksCapProperties{

    public static final TypedRegistry.EntryType<CapProperties> TYPE = TypedRegistry.newType(CloudcapCapProperties::new);

    public CloudcapCapProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicCapCenterBlock createDynamicCapCenter(BlockBehaviour.Properties properties) {
        return new DynamicCapCenterBlock(this, properties){
            @Override
            protected void generateCap(int newAge, Level pLevel, HugeMushroomSpecies species, BlockPos newPos, BlockPos currentPos, int currentAge, BlockPos rootPos) {
                DynamicCapBlock capBlock = (DynamicCapBlock)this.properties.getDynamicCapBlock().orElse(null);
                if (capBlock != null) {
                    Set<CoordUtils.Surround> surrounds = new HashSet<>();
                    if (currentPos != newPos || currentAge != newAge) {
                        species.getMushroomShapeKit().clearMushroomCap(new MushroomCapContext(pLevel, currentPos, species, currentAge));
                        for (CoordUtils.Surround surr : CoordUtils.Surround.values()){
                            BlockPos pos = currentPos.below().offset(surr.getOffset());
                            if (pLevel.getBlockState(pos).is(ReduxBlocks.CLOUDCAP_SPORES.get())){
                                surrounds.add(surr);
                                pLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                            }
                        }
                    }

                    species.getMushroomShapeKit().generateMushroomCap(new MushroomCapContext(pLevel, newPos, species, newAge));
                    for (CoordUtils.Surround surr : surrounds){
                        BlockPos pos = newPos.below().offset(surr.getOffset());
                        if (pLevel.isEmptyBlock(pos)){
                            pLevel.setBlock(pos, ReduxBlocks.CLOUDCAP_SPORES.get().defaultBlockState(), 3);
                        }
                    }
                }
            }

            @Override
            public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
                super.destroy(pLevel, pPos, pState);
            }
        };
    }
}
