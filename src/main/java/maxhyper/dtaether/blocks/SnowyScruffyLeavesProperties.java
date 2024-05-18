package maxhyper.dtaether.blocks;

import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.block.leaves.ScruffyLeavesProperties;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.MutableLazyValue;
import maxhyper.dtaether.data.SnowyLeavesStateGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SnowyScruffyLeavesProperties extends ScruffyLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(SnowyScruffyLeavesProperties::new);

    public SnowyScruffyLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    protected final MutableLazyValue<Generator<DTBlockStateProvider, LeavesProperties>> stateGenerator =
            MutableLazyValue.supplied(SnowyLeavesStateGenerator::new);

    @Override
    public void generateStateData(DTBlockStateProvider provider) {
        // Generate leaves block state and model.
        this.stateGenerator.get().generate(provider, this);
    }

    private float scruffyLeafChance = 0.66f;
    private int scruffyMaxHydro = 1;

    @Override
    public void setLeafChance (float leafChance){
        this.scruffyLeafChance = leafChance;
    }
    @Override
    public void setMaxHydro (int maxHydro) {
        this.scruffyMaxHydro = maxHydro;
    }

    @Override
    public BlockState getDynamicLeavesState(int hydro) {
        BlockState state = dynamicLeavesBlockHydroStates[Mth.clamp(hydro, 0, LeavesProperties.maxHydro)];
        if (state != null) state = state.setValue(SnowyLeavesBlock.SNOWY, false);
        return Optional.ofNullable(state)
                .orElse(Blocks.AIR.defaultBlockState());
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new SnowyLeavesBlock(this, properties, scruffyLeafChance, scruffyMaxHydro);
    }
}
