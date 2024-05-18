package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.rooty.RootyBlock;
import com.ferreusveritas.dynamictrees.block.rooty.SoilProperties;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AltTintSoilProperties extends SoilProperties {

    public static final TypedRegistry.EntryType<SoilProperties> TYPE = TypedRegistry.newType(AltTintSoilProperties::new);

    public AltTintSoilProperties (final ResourceLocation registryName){
        super(null, registryName);
        modelOverrides.put(ROOTS,DynamicTrees.location("block/roots_tint_2"));
    }

    @Override
    protected RootyBlock createBlock(BlockBehaviour.Properties blockProperties) {
        return new RootyBlock(this, blockProperties){
            @Override
            public int colorMultiplier(BlockColors blockColors, BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
                final int white = 0xFFFFFFFF;
                return switch (tintIndex) {
                    case 1 -> blockColors.getColor(getPrimitiveSoilState(state), level, pos, tintIndex);
                    case 2 -> state.getBlock() instanceof RootyBlock ? rootColor(state, level, pos) : white;
                    default -> white;
                };
            }
        };
    }

}
